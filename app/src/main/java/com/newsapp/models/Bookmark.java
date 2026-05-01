package com.newsapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "bookmarks")
public class Bookmark implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @NonNull
    private String userId; // The user who bookmarked this article
    
    private String title;
    private String description;
    private String content;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String author;
    private String sourceName; // Flattened source name

    public Bookmark() {}

    public Bookmark(@NonNull String userId, NewsArticle article) {
        this.userId = userId;
        this.title = article.getTitle();
        this.description = article.getDescription();
        this.content = article.getContent();
        this.url = article.getUrl();
        this.urlToImage = article.getUrlToImage();
        this.publishedAt = article.getPublishedAt();
        this.author = article.getAuthor();
        if (article.getSource() != null) {
            this.sourceName = article.getSource().getName();
        }
    }

    public NewsArticle toNewsArticle() {
        NewsArticle article = new NewsArticle();
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(content);
        article.setUrl(url);
        article.setUrlToImage(urlToImage);
        article.setPublishedAt(publishedAt);
        article.setAuthor(author);
        article.setBookmarked(true);
        
        NewsArticle.Source source = new NewsArticle.Source();
        source.setName(sourceName);
        article.setSource(source);
        
        return article;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    @NonNull
    public String getUserId() { return userId; }
    public void setUserId(@NonNull String userId) { this.userId = userId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public String getUrlToImage() { return urlToImage; }
    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }
    
    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
}
