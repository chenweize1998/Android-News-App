package com.example.newstoday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.room.ColumnInfo;
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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;

@Entity
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
    private String oriFollowig;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] avatar;


    @Ignore
    private ArraySet<String> followig;

    @Ignore
    private Bitmap oriAvatar;

    public User(String email, String name, String password, @Nullable String oriFollowig, @Nullable byte[] avatar){
        this.email = email;
        this.name = name;
        this.password = password;
        this.oriFollowig = oriFollowig;
        this.avatar = avatar;
        if(oriFollowig != null)
            this.followig = new ArraySet<String>(Arrays.asList(oriFollowig.split(",")));
        else
            this.followig = new ArraySet<>();
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

    public String getOriFollowig(){
        return oriFollowig;
    }

    public ArraySet<String> getFollowig(){
        return this.followig;
    }

    public byte[] getAvatar(){
        return this.avatar;
    }

    public Bitmap getOriAvatar(){
        return ImageHandler.bytes2Bitmap(this.avatar);
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

    public void setOriFollowig(String oriFollowig){
        this.oriFollowig = oriFollowig;
    }

    /**
     * 关注的时候调用
     * @param email
     */
    public void addFollowig(String email){
        this.oriFollowig = this.oriFollowig + "," + email;
        this.followig.add(email);
    }

    public void setAvatar(byte[] bytes){
        this.avatar = bytes;
    }

    /**
     * 必须调用此方法来设置头像
     * */
    public void setOriAvatar(Bitmap oriAvatar){
        this.avatar = ImageHandler.bitmap2Bytes(oriAvatar);
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

class ImageHandler{

    public static byte[] bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(byte[] b){
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
