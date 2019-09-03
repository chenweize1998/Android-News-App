package com.example.newstoday;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Entity
@TypeConverters({ImageConverter.class})
public class UserMessage {
    @NonNull
    @PrimaryKey
    private String messageID; // 此ID可以通过 email+"&"+System.currentTimeMillis()构造message的唯一标识;

    private String email;
    private String content;
    @Nullable
    private Bitmap image;

    UserMessage(String messageID, String email, String content, Bitmap image){
        this.messageID = messageID;
        this.email = email;
        this.content = content;
        this.image = image;
    }

    public String getMessageID(){
        return this.messageID;
    }

    public String getEmail(){
        return this.email;
    }

    public String getContent(){
        return this.content;
    }

    public Bitmap getImage(){
        return this.image;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }
}

@Dao
interface UserMessageDao{

    @Query("SELECT * FROM UserMessage")
    UserMessage[] getAllUserMessage();

    @Query("SELECT * FROM UserMessage WHERE email IN (:emails)")
    UserMessage[] getCerternUserMessageByUserEmail(String... emails);

    @Query("DELETE FROM UserMessage WHERE messageID IN (:messageIDs)")
    void deleteMessageByMessageID(String... messageIDs);

    @Query("DELETE FROM UserMessage WHERE email IN (:emails)")
    void deleteMessageByUserEmail(String... emails);

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insert(UserMessage... userMessages);

}

@Database(entities = {UserMessage.class}, version = 1)
abstract class UserMessageDB extends RoomDatabase{
    public abstract UserMessageDao userMessageDao();

    public static UserMessageDB getUserMessageDB(Context context, String name){
        return Room.databaseBuilder(context.getApplicationContext(),
                UserMessageDB.class, name).build();
    }
}


