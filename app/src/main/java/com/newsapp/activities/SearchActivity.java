package com.newsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.BuildConfig;
import com.newsapp.R;
import com.newsapp.adapters.NewsAdapter;
import com.newsapp.models.NewsArticle;
import com.newsapp.models.NewsResponse;
import com.newsapp.utils.Constants;
import com.newsapp.utils.RetrofitClient;
import com.newsapp.utils.SessionManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements NewsAdapter.OnArticleClickListener {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Search News");
        }

        sessionManager = new SessionManager(this);

        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_search);
        progressBar = findViewById(R.id.progress_bar);
        tvEmpty = findViewById(R.id.tv_empty);

        adapter = new NewsAdapter(this, new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void performSearch(String query) {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        adapter.clearArticles();

        String language = sessionManager.getPreferredLanguage();

        RetrofitClient.getInstance().getApiService()
                .searchNews(query, language, BuildConfig.NEWS_API_KEY, 30)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null &&
                                response.body().getArticles() != null) {
                            adapter.addArticles(response.body().getArticles());
                            if (response.body().getArticles().isEmpty()) {
                                tvEmpty.setText("No results for \"" + query + "\"");
                                tvEmpty.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tvEmpty.setText("Search failed. Try again.");
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        tvEmpty.setText("Network error. Check your connection.");
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onArticleClick(NewsArticle article) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE, article);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
