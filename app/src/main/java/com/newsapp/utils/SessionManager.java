package com.newsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "NewsAppSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_LANGUAGE = "preferredLanguage";
    private static final String KEY_CATEGORY = "preferredCategory";
    private static final String KEY_DARK_MODE = "darkMode";
    private static final String KEY_COUNTRY = "preferredCountry";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveUserDetails(String userId, String name, String email) {
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserId() { return pref.getString(KEY_USER_ID, ""); }
    public String getUserName() { return pref.getString(KEY_USER_NAME, "User"); }
    public String getUserEmail() { return pref.getString(KEY_USER_EMAIL, ""); }

    public void setPreferredLanguage(String language) {
        editor.putString(KEY_LANGUAGE, language);
        editor.apply();
    }

    public String getPreferredLanguage() {
        return pref.getString(KEY_LANGUAGE, "en");
    }

    public void setPreferredCategory(String category) {
        editor.putString(KEY_CATEGORY, category);
        editor.apply();
    }

    public String getPreferredCategory() {
        return pref.getString(KEY_CATEGORY, "general");
    }

    public void setPreferredCountry(String country) {
        editor.putString(KEY_COUNTRY, country);
        editor.apply();
    }

    public String getPreferredCountry() {
        return pref.getString(KEY_COUNTRY, "us");
    }

    public void setDarkMode(boolean darkMode) {
        editor.putBoolean(KEY_DARK_MODE, darkMode);
        editor.apply();
    }

    public boolean isDarkMode() {
        return pref.getBoolean(KEY_DARK_MODE, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
