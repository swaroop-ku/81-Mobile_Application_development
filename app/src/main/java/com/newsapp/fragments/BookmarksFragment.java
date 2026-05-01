package com.newsapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.R;
import com.newsapp.activities.NewsDetailActivity;
import com.newsapp.adapters.NewsAdapter;
import com.newsapp.database.AppDatabase;
import com.newsapp.database.BookmarkDao;
import com.newsapp.models.Bookmark;
import com.newsapp.models.NewsArticle;
import com.newsapp.utils.Constants;
import com.newsapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment implements
        NewsAdapter.OnArticleClickListener,
        NewsAdapter.OnBookmarkClickListener {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private View emptyLayout;          // The LinearLayout acting as empty state
    private BookmarkDao bookmarkDao;
    private SessionManager sessionManager;
    private String currentUserId;
    private List<NewsArticle> articleList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        bookmarkDao = AppDatabase.getInstance(requireContext()).bookmarkDao();
        sessionManager = new SessionManager(requireContext());
        currentUserId = sessionManager.getUserId();
        if (currentUserId != null && currentUserId.isEmpty()) currentUserId = null;

        recyclerView = view.findViewById(R.id.recycler_bookmarks);
        emptyLayout = view.findViewById(R.id.tv_empty_bookmarks);  // This is a LinearLayout

        articleList = new ArrayList<>();
        adapter = new NewsAdapter(requireContext(), articleList, this);
        adapter.setOnBookmarkClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        loadBookmarks();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload bookmarks each time user returns to this tab
        loadBookmarks();
    }

    private void loadBookmarks() {
        if (currentUserId == null) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        List<Bookmark> bookmarkList = bookmarkDao.getBookmarksForUser(currentUserId);
        List<NewsArticle> articles = new ArrayList<>();
        for (Bookmark b : bookmarkList) {
            articles.add(b.toNewsArticle());
        }

        adapter.clearArticles();
        if (articles.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.addArticles(articles);
        }
    }

    @Override
    public void onArticleClick(NewsArticle article) {
        Intent intent = new Intent(requireContext(), NewsDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE, article);
        startActivity(intent);
    }

    @Override
    public void onBookmarkClick(NewsArticle article, int position) {
        // In the bookmarks screen, clicking the bookmark icon removes it
        if (currentUserId == null) return;
        bookmarkDao.deleteBookmarkByUrl(currentUserId, article.getUrl());
        articleList.remove(position);
        adapter.notifyItemRemoved(position);
        Toast.makeText(requireContext(), "Removed from bookmarks", Toast.LENGTH_SHORT).show();

        if (articleList.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
