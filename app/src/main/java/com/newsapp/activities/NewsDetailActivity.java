package com.newsapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newsapp.R;
import com.newsapp.database.AppDatabase;
import com.newsapp.database.BookmarkDao;
import com.newsapp.models.Bookmark;
import com.newsapp.models.NewsArticle;
import com.newsapp.utils.Constants;
import com.newsapp.utils.SessionManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsDetailActivity extends AppCompatActivity {

    private NewsArticle article;
    private boolean isBookmarked = false;
    private MenuItem bookmarkItem;
    private BookmarkDao bookmarkDao;
    private SessionManager sessionManager;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        bookmarkDao = AppDatabase.getInstance(this).bookmarkDao();
        sessionManager = new SessionManager(this);
        String rawId = sessionManager.getUserId();
        currentUserId = (rawId != null && !rawId.isEmpty()) ? rawId : null;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        article = (NewsArticle) getIntent().getSerializableExtra(Constants.EXTRA_ARTICLE);
        if (article == null) { finish(); return; }

        isBookmarked = article.isBookmarked();

        ImageView ivThumbnail = findViewById(R.id.iv_thumbnail);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvSource = findViewById(R.id.tv_source);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvAuthor = findViewById(R.id.tv_author);
        TextView tvContent = findViewById(R.id.tv_content);
        FloatingActionButton fabShare = findViewById(R.id.fab_share);
        FloatingActionButton fabOpenBrowser = findViewById(R.id.fab_open_browser);

        // Populate data
        Glide.with(this)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.placeholder_news)
                .error(R.drawable.placeholder_news)
                .timeout(30000)
                .centerCrop()
                .into(ivThumbnail);

        tvTitle.setText(article.getTitle());
        tvSource.setText(article.getSource() != null ? article.getSource().getName() : "Unknown Source");
        tvAuthor.setText(article.getAuthor() != null ? "By " + article.getAuthor() : "");
        tvDate.setText(formatDate(article.getPublishedAt()));

        String content = article.getContent() != null ? article.getContent() : article.getDescription();
        if (content != null) {
            // Remove the "[+N chars]" suffix that NewsAPI adds
            content = content.replaceAll("\\[\\+\\d+ chars\\]", "").trim();
        }
        tvContent.setText(content != null ? content : "Tap 'Open in Browser' to read the full article.");

        // SHARE
        fabShare.setOnClickListener(v -> shareArticle());

        // Open in browser
        fabOpenBrowser.setOnClickListener(v -> {
            if (article.getUrl() != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                startActivity(Intent.createChooser(browserIntent, "Open with"));
            }
        });

        checkBookmarkStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        bookmarkItem = menu.findItem(R.id.action_bookmark);
        updateBookmarkIcon();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_bookmark) {
            toggleBookmark();
            return true;
        } else if (id == R.id.action_share) {
            shareArticle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareArticle() {
        String shareText = article.getTitle() + "\n\n" +
                (article.getDescription() != null ? article.getDescription() + "\n\n" : "") +
                "Read more: " + article.getUrl();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(shareIntent, "Share news via..."));
    }

    private void toggleBookmark() {
        if (currentUserId == null) {
            Toast.makeText(this, "Login to bookmark articles", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isBookmarked) {
            bookmarkDao.deleteBookmarkByUrl(currentUserId, article.getUrl());
            isBookmarked = false;
            updateBookmarkIcon();
            Toast.makeText(this, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
        } else {
            Bookmark bookmark = new Bookmark(currentUserId, article);
            bookmarkDao.insertBookmark(bookmark);
            isBookmarked = true;
            updateBookmarkIcon();
            Toast.makeText(this, "Bookmarked!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBookmarkStatus() {
        if (currentUserId == null || article.getUrl() == null) return;

        Bookmark bookmark = bookmarkDao.getBookmark(currentUserId, article.getUrl());
        isBookmarked = (bookmark != null);
        updateBookmarkIcon();
    }

    private void updateBookmarkIcon() {
        if (bookmarkItem != null) {
            bookmarkItem.setIcon(isBookmarked ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
        }
    }

    private String formatDate(String isoDate) {
        if (isoDate == null) return "";
        try {
            SimpleDateFormat inputFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            SimpleDateFormat outputFmt = new SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault());
            Date date = inputFmt.parse(isoDate);
            return date != null ? outputFmt.format(date) : isoDate;
        } catch (ParseException e) {
            return isoDate;
        }
    }
}
