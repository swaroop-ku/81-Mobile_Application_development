package com.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newsapp.R;
import com.newsapp.models.NewsArticle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static final int VIEW_TYPE_FEATURED = 0;
    private static final int VIEW_TYPE_NORMAL = 1;

    private final Context context;
    private final List<NewsArticle> articles;
    private final OnArticleClickListener listener;
    private OnBookmarkClickListener bookmarkListener;

    public interface OnArticleClickListener {
        void onArticleClick(NewsArticle article);
    }

    public interface OnBookmarkClickListener {
        void onBookmarkClick(NewsArticle article, int position);
    }

    public NewsAdapter(Context context, List<NewsArticle> articles, OnArticleClickListener listener) {
        this.context = context;
        this.articles = articles;
        this.listener = listener;
    }

    public void setOnBookmarkClickListener(OnBookmarkClickListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_FEATURED : VIEW_TYPE_NORMAL;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == VIEW_TYPE_FEATURED ?
                R.layout.item_news_featured : R.layout.item_news;
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articles.get(position);
        holder.bind(article);
        holder.itemView.setOnClickListener(v -> listener.onArticleClick(article));

        // Bookmark button
        if (holder.btnBookmark != null) {
            // Set correct icon based on article's bookmarked state
            holder.btnBookmark.setImageResource(
                    article.isBookmarked() ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline
            );

            holder.btnBookmark.setOnClickListener(v -> {
                if (bookmarkListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_ID) {
                        bookmarkListener.onBookmarkClick(article, adapterPosition);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void addArticles(List<NewsArticle> newArticles) {
        int start = articles.size();
        articles.addAll(newArticles);
        notifyItemRangeInserted(start, newArticles.size());
    }

    public void clearArticles() {
        articles.clear();
        notifyDataSetChanged();
    }

    /** Update a single item's bookmark state without full rebind */
    public void notifyItemBookmarkChanged(int position) {
        notifyItemChanged(position);
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle, tvSource, tvDate, tvDescription;
        CardView cardView;
        ImageButton btnBookmark;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSource = itemView.findViewById(R.id.tv_source);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDescription = itemView.findViewById(R.id.tv_description);
            cardView = itemView.findViewById(R.id.card_view);
            btnBookmark = itemView.findViewById(R.id.btn_bookmark);
        }

        void bind(NewsArticle article) {
            tvTitle.setText(article.getTitle());

            if (tvSource != null && article.getSource() != null) {
                tvSource.setText(article.getSource().getName());
            }

            if (tvDate != null) {
                tvDate.setText(formatDate(article.getPublishedAt()));
            }

            if (tvDescription != null && article.getDescription() != null) {
                tvDescription.setText(article.getDescription());
            }

            String imageUrl = article.getUrlToImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.placeholder_news)
                        .error(R.drawable.placeholder_news)
                        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.DATA)
                        .timeout(20000)
                        .centerCrop()
                        .into(ivThumbnail);
            } else {
                ivThumbnail.setImageResource(R.drawable.placeholder_news);
            }
        }

        private String formatDate(String isoDate) {
            if (isoDate == null) return "";
            try {
                SimpleDateFormat inputFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                SimpleDateFormat outputFmt = new SimpleDateFormat("MMM dd", Locale.getDefault());
                Date date = inputFmt.parse(isoDate);
                return date != null ? outputFmt.format(date) : "";
            } catch (ParseException e) {
                return "";
            }
        }
    }
}
