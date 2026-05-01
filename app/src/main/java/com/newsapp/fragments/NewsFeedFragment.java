package com.newsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.newsapp.BuildConfig;
import com.newsapp.R;
import com.newsapp.activities.NewsDetailActivity;
import com.newsapp.adapters.NewsAdapter;
import com.newsapp.database.AppDatabase;
import com.newsapp.database.BookmarkDao;
import com.newsapp.models.Bookmark;
import com.newsapp.models.NewsArticle;
import com.newsapp.models.NewsResponse;
import com.newsapp.utils.Constants;
import com.newsapp.utils.RetrofitClient;
import com.newsapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedFragment extends Fragment implements
        NewsAdapter.OnArticleClickListener,
        NewsAdapter.OnBookmarkClickListener {

    private static final String ARG_CATEGORY = "category";
    private static final String ARG_LANGUAGE = "language";
    private static final String ARG_COUNTRY = "country";

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefresh;
    private ShimmerFrameLayout shimmerLayout;
    private TextView tvError;

    private BookmarkDao bookmarkDao;
    private SessionManager sessionManager;
    private String currentUserId;

    private String category, language, country;
    private int currentPage = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    public static NewsFeedFragment newInstance(String category, String language, String country) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_LANGUAGE, language);
        args.putString(ARG_COUNTRY, country);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY, "general");
            language = getArguments().getString(ARG_LANGUAGE, "en");
            country = getArguments().getString(ARG_COUNTRY, "us");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        bookmarkDao = AppDatabase.getInstance(requireContext()).bookmarkDao();
        sessionManager = new SessionManager(requireContext());
        currentUserId = sessionManager.getUserId();
        if (currentUserId != null && currentUserId.isEmpty()) currentUserId = null;

        recyclerView = view.findViewById(R.id.recycler_view);
        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        shimmerLayout = view.findViewById(R.id.shimmer_layout);
        tvError = view.findViewById(R.id.tv_error);

        newsAdapter = new NewsAdapter(requireContext(), new ArrayList<>(), this);
        newsAdapter.setOnBookmarkClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(newsAdapter);

        // Infinite scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
                    currentPage++;
                    fetchNews(false);
                }
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(() -> {
            currentPage = 1;
            isLastPage = false;
            newsAdapter.clearArticles();
            fetchNews(false);
        });

        fetchNews(true);
        return view;
    }

    /** Mark articles already bookmarked by current user */
    private void markBookmarkedArticles(List<NewsArticle> articles) {
        if (currentUserId == null || articles == null) return;
        List<Bookmark> userBookmarks = bookmarkDao.getBookmarksForUser(currentUserId);
        for (NewsArticle article : articles) {
            for (Bookmark b : userBookmarks) {
                if (b.getUrl() != null && b.getUrl().equals(article.getUrl())) {
                    article.setBookmarked(true);
                    break;
                }
            }
        }
    }

    private void fetchNews(boolean showShimmer) {
        if (showShimmer) {
            shimmerLayout.setVisibility(View.VISIBLE);
            shimmerLayout.startShimmer();
            recyclerView.setVisibility(View.GONE);
        }
        isLoading = true;
        tvError.setVisibility(View.GONE);

        Call<NewsResponse> call;

        if (language.equals("en")) {
            // English: use top-headlines by country only (language param breaks the API for top-headlines)
            call = RetrofitClient.getInstance().getApiService().getTopHeadlines(
                    country, category,
                    BuildConfig.NEWS_API_KEY, Constants.PAGE_SIZE, currentPage);
        } else {
            // Non-English: use everything endpoint with category as query keyword
            call = RetrofitClient.getInstance().getApiService().getEverything(
                    category, language, "publishedAt",
                    BuildConfig.NEWS_API_KEY, Constants.PAGE_SIZE, currentPage);
        }

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                isLoading = false;
                swipeRefresh.setRefreshing(false);
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                if (response.isSuccessful() && response.body() != null) {
                    List<NewsArticle> articles = response.body().getArticles();
                    if (articles != null && !articles.isEmpty()) {
                        // Filter out articles with [Removed] title (NewsAPI placeholder)
                        List<NewsArticle> filtered = new java.util.ArrayList<>();
                        for (NewsArticle a : articles) {
                            if (a.getTitle() != null && !a.getTitle().equals("[Removed]")) {
                                filtered.add(a);
                            }
                        }
                        if (!filtered.isEmpty()) {
                            markBookmarkedArticles(filtered);
                            newsAdapter.addArticles(filtered);
                            if (filtered.size() < Constants.PAGE_SIZE) isLastPage = true;
                        } else {
                            isLastPage = true;
                            if (newsAdapter.getItemCount() == 0)
                                showError("No news available for this selection.");
                        }
                    } else if (language.equals("en") && newsAdapter.getItemCount() == 0) {
                        // Fallback: top-headlines returned nothing for this country, try everything
                        fetchEverythingFallback();
                    } else {
                        isLastPage = true;
                        if (newsAdapter.getItemCount() == 0)
                            showError("No news available for this category.");
                    }
                } else {
                    if (newsAdapter.getItemCount() == 0)
                        showError("Failed to load news. Please try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                isLoading = false;
                swipeRefresh.setRefreshing(false);
                shimmerLayout.stopShimmer();
                shimmerLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                showError("Network error. Check your connection.");
            }
        });
    }

    /** Fallback for English when top-headlines returns nothing for selected country */
    private void fetchEverythingFallback() {
        RetrofitClient.getInstance().getApiService().getEverything(
                category, "en", "publishedAt",
                BuildConfig.NEWS_API_KEY, Constants.PAGE_SIZE, 1
        ).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null) {
                    List<NewsArticle> articles = response.body().getArticles();
                    if (articles != null && !articles.isEmpty()) {
                        List<NewsArticle> filtered = new java.util.ArrayList<>();
                        for (NewsArticle a : articles) {
                            if (a.getTitle() != null && !a.getTitle().equals("[Removed]")) {
                                filtered.add(a);
                            }
                        }
                        markBookmarkedArticles(filtered);
                        newsAdapter.addArticles(filtered);
                    } else {
                        showError("No news available for this country.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                showError("Network error. Check your connection.");
            }
        });
    }

    private void showError(String msg) {
        if (newsAdapter.getItemCount() == 0) {
            tvError.setText(msg);
            tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onArticleClick(NewsArticle article) {
        Intent intent = new Intent(requireContext(), NewsDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE, article);
        startActivity(intent);
        requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }

    @Override
    public void onBookmarkClick(NewsArticle article, int position) {
        if (currentUserId == null) {
            Toast.makeText(requireContext(), "Login to bookmark articles", Toast.LENGTH_SHORT).show();
            return;
        }

        if (article.isBookmarked()) {
            // Remove bookmark
            bookmarkDao.deleteBookmarkByUrl(currentUserId, article.getUrl());
            article.setBookmarked(false);
            newsAdapter.notifyItemBookmarkChanged(position);
            Toast.makeText(requireContext(), "Removed from bookmarks", Toast.LENGTH_SHORT).show();
        } else {
            // Add bookmark
            Bookmark bookmark = new Bookmark(currentUserId, article);
            bookmarkDao.insertBookmark(bookmark);
            article.setBookmarked(true);
            newsAdapter.notifyItemBookmarkChanged(position);
            Toast.makeText(requireContext(), "Bookmarked!", Toast.LENGTH_SHORT).show();
        }
    }
}
