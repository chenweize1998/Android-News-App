package com.example.newstoday;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM News")
    News[] getAllNews();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News... news);

    @Delete
    void delete(News... news);

}

