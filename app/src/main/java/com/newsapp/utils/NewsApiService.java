package com.newsapp.utils;

import com.newsapp.models.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    // Top headlines by country & category (no language filter — API doesn't support it)
    @GET("v2/top-headlines")
    Call<NewsResponse> getTopHeadlines(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );

    // Everything by language, query & sortBy — works for all languages and countries
    @GET("v2/everything")
    Call<NewsResponse> getEverything(
            @Query("q") String query,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );

    // Search articles
    @GET("v2/everything")
    Call<NewsResponse> searchNews(
            @Query("q") String query,
            @Query("language") String language,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize
    );
}
