package com.example.newstoday;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {News.class}, version = 1)
public abstract class AppDB extends RoomDatabase {

    public abstract NewsDao newsDao();

    private static AppDB INSTANCE = null;

    public static AppDB getAppDB(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDB.class,
                                    "test").build();

        }
        return INSTANCE;
    }

}
