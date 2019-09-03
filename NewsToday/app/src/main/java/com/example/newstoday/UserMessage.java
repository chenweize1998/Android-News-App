package com.example.newstoday;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
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

@Entity
public class UserMessage {    @NonNull
@PrimaryKey
private String messageID; // 此ID可以通过 email+"&"+System.currentTimeMillis()构造message的唯一标识;

    private String email;
    private String content;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;


    @Ignore
    private Bitmap oriImage;

    UserMessage(String messageID, String email, String content, byte[] image){
        this.messageID = email+"&"+System.currentTimeMillis();
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

    public byte[] getImage(){
        return this.image;
    }

    public Bitmap getOriImage(){
        return ImageHandler.bytes2Bitmap(this.image);
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

    public void setImage(byte[] image){
        this.image = image;
    }

    public void setOriImage(Bitmap oriImage){
        this.image = ImageHandler.bitmap2Bytes(oriImage);
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


