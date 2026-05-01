package com.newsapp;

import android.app.Application;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executors;

public class NewsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Clear Glide disk cache in background so stale/failed image entries
        // don't block fresh network requests on next launch
        Executors.newSingleThreadExecutor().execute(() ->
                Glide.get(getApplicationContext()).clearDiskCache()
        );
    }
}
