package com.newsapp.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
    // Language codes and display names
    public static final Map<String, String> LANGUAGES = new LinkedHashMap<>();
    static {
        LANGUAGES.put("en", "English");
        LANGUAGES.put("hi", "Hindi (हिन्दी)");
        LANGUAGES.put("ar", "Arabic (العربية)");
        LANGUAGES.put("de", "German (Deutsch)");
        LANGUAGES.put("fr", "French (Français)");
        LANGUAGES.put("es", "Spanish (Español)");
        LANGUAGES.put("it", "Italian (Italiano)");
        LANGUAGES.put("pt", "Portuguese (Português)");
        LANGUAGES.put("ru", "Russian (Русский)");
        LANGUAGES.put("zh", "Chinese (中文)");
        LANGUAGES.put("ja", "Japanese (日本語)");
        LANGUAGES.put("ko", "Korean (한국어)");
        LANGUAGES.put("nl", "Dutch (Nederlands)");
        LANGUAGES.put("no", "Norwegian (Norsk)");
        LANGUAGES.put("sv", "Swedish (Svenska)");
    }

    // News categories
    public static final String[] CATEGORIES = {
        "general", "business", "entertainment", "health",
        "science", "sports", "technology"
    };

    // Country codes for top-headlines
    public static final Map<String, String> COUNTRIES = new LinkedHashMap<>();
    static {
        COUNTRIES.put("us", "United States");
        COUNTRIES.put("in", "India");
        COUNTRIES.put("gb", "United Kingdom");
        COUNTRIES.put("au", "Australia");
        COUNTRIES.put("ca", "Canada");
        COUNTRIES.put("de", "Germany");
        COUNTRIES.put("fr", "France");
        COUNTRIES.put("jp", "Japan");
        COUNTRIES.put("cn", "China");
        COUNTRIES.put("br", "Brazil");
        COUNTRIES.put("ru", "Russia");
        COUNTRIES.put("ae", "UAE");
        COUNTRIES.put("za", "South Africa");
    }

    // Intent extras
    public static final String EXTRA_ARTICLE = "article";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_LANGUAGE = "language";

    // Page size
    public static final int PAGE_SIZE = 20;

    // Firestore collections
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_BOOKMARKS = "bookmarks";
}
