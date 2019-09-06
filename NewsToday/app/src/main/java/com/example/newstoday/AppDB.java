package com.example.newstoday;


import android.content.Context;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.Update;

@Dao
interface NewsDao {

    @Query("SELECT * FROM News")
    News[] getAllNews();

    @Query("DELETE FROM News")
    void clear();

    @Query("SELECT newsID FROM News")
    String[] getAllNewsID();

    @Query("SELECT * FROM News WHERE publisher in (:email)")
    News[] getNewsByEmail(String... email);

    @Query("DELETE FROM News WHERE publisher in (:email)")
    void deleteNewsByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(News... news);

    @Update
    void update(News... news);

    @Delete
    void delete(News... news);

}

@Database(entities = {News.class}, version = 1)
public abstract class AppDB extends RoomDatabase {

    public abstract NewsDao newsDao();

    public static AppDB getAppDB(Context context, String name){
        return  Room.databaseBuilder(context.getApplicationContext(), AppDB.class,
                name).build();
    }

}