package com.example.newstoday;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NewsDao {

//    @Query("SELECT TITLE FROM News WHERE category IN (:categories)")
//    News[] getNewsByCat(String[] categories);

    @Insert
    void insert(News news);

}

