package com.newsapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.newsapp.models.Bookmark;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void insertBookmark(Bookmark bookmark);

    @Delete
    void deleteBookmark(Bookmark bookmark);

    @Query("SELECT * FROM bookmarks WHERE userId = :userId")
    List<Bookmark> getBookmarksForUser(String userId);

    @Query("SELECT * FROM bookmarks WHERE userId = :userId AND url = :url LIMIT 1")
    Bookmark getBookmark(String userId, String url);

    @Query("DELETE FROM bookmarks WHERE userId = :userId AND url = :url")
    void deleteBookmarkByUrl(String userId, String url);
}
