package com.newsapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey
    @NonNull
    private String uid;
    private String name;
    private String email;
    private String password;
    private String photoUrl;
    private String preferredLanguage;
    private String preferredCategory;

    public User() {} 

    public User(@NonNull String uid, String name, String email, String password) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.preferredLanguage = "en";
        this.preferredCategory = "general";
    }

    @NonNull
    public String getUid() { return uid; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhotoUrl() { return photoUrl; }
    public String getPreferredLanguage() { return preferredLanguage; }
    public String getPreferredCategory() { return preferredCategory; }

    public void setUid(@NonNull String uid) { this.uid = uid; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }
    public void setPreferredCategory(String preferredCategory) { this.preferredCategory = preferredCategory; }
}
