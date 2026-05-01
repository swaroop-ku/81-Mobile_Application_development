package com.newsapp.models;

import java.io.Serializable;

public class NewsArticle implements Serializable {
    private String title;
    private String description;
    private String content;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String author;
    private Source source;
    private boolean isBookmarked;

    public static class Source implements Serializable {
        private String id;
        private String name;

        public String getId() { return id; }
        public String getName() { return name; }
        public void setId(String id) { this.id = id; }
        public void setName(String name) { this.name = name; }
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getContent() { return content; }
    public String getUrl() { return url; }
    public String getUrlToImage() { return urlToImage; }
    public String getPublishedAt() { return publishedAt; }
    public String getAuthor() { return author; }
    public Source getSource() { return source; }
    public boolean isBookmarked() { return isBookmarked; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setContent(String content) { this.content = content; }
    public void setUrl(String url) { this.url = url; }
    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }
    public void setAuthor(String author) { this.author = author; }
    public void setSource(Source source) { this.source = source; }
    public void setBookmarked(boolean bookmarked) { isBookmarked = bookmarked; }
}
