package com.example.newstoday;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM News")
    News[] getAllNews();

    @Query("select * from News where newsID = :ID limit 1")
    News getOneNews(String ID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News... news);

    @Delete
    void delete(News... news);

}

