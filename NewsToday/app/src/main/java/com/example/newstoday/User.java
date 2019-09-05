package com.example.newstoday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.ArraySet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;

@Entity
@TypeConverters({ImageConverter.class, SetConverter.class})
public class User {

    /**
     * User自己是没有对自己发布消息的存储权的，所以在本类中没有message域
     * 对于转发的消息也同样如此
     * */

    @NonNull
    @PrimaryKey
    private String email;

    private String name;
    private String password;
    private String avatar;
    @Nullable
    private ArraySet<String> followig;


    public User(String email, String name, String password, @Nullable ArraySet<String> followig, @Nullable String avatar){
        this.email = email;
        this.name = name;
        this.password = password;
        if(followig == null)
            this.followig = new ArraySet<>();
        else
            this.followig = followig;
        this.avatar = avatar;
    }

    public String getEmail(){
        return this.email;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }

    public ArraySet<String> getFollowig(){
        return followig;
    }

    public String getAvatar(){
        return this.avatar;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setFollowig(ArraySet<String> followig){
        this.followig = followig;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public void addFollowig(String email){
        this.followig.add(email);
    }

    public void deleteFollowig(String email){
        this.followig.remove(email);
    }
}

@Dao
interface UserDao{

    @Query("SELECT * FROM User")
    User[] getAllUsers();

    @Query("SELECT * FROM User WHERE email IN (:email)")
    User[] getUserByEmail(String... email);

    @Query("DELETE FROM User")
    void clear();

    @Query("SELECT password FROM User WHERE email IN (:email)")
    String getPassword(String...email);

    @Update
    void updateUser(User user);

    @Delete
    void delete(User... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... users);

}

@Database(entities = {User.class}, version = 1)
abstract class UserDB extends RoomDatabase {

    public abstract UserDao userDao();

    public static UserDB getUserDB(Context context, String name){
        return Room.databaseBuilder(context.getApplicationContext(), UserDB.class,
                name).build();
    }

}


