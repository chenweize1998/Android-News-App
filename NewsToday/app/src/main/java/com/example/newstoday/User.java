package com.example.newstoday;

import android.content.Context;

import androidx.annotation.NonNull;
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

@Entity
class User {
    @NonNull
    @PrimaryKey
    String email;

    String name;
    String password;
    String oriFollowig;
    String oriMessage;

    @Ignore
    String[] followig;
    String[] message;

    User(String email, String name, String password, String oriFollowig, String oriMessage){
        this.email = email;
        this.name = name;
        this.password = password;
        this.oriFollowig = oriFollowig;
        this.oriMessage = oriMessage;
    }

    String getEmail(){
        return this.email;
    }

    String getName(){
        return this.name;
    }

    String getPassword(){
        return this.password;
    }

    String getOriFollowig(){
        return oriFollowig;
    }

    String[] getFollowig(){
        return oriFollowig.split(",");
    }

    String getOriMessage(){
        return oriMessage;
    }

    String[] getMessage(){
        return oriMessage.split("#####");
    }

    void setEmail(String email){
        this.email = email;
    }

    void setName(String name){
        this.name = name;
    }

    void setPassword(String password){
        this.password = password;
    }

    void setOriFollowig(String oriFollowig){
        this.oriFollowig = oriFollowig;
    }

    void setOriMessage(String oriMessage){
        this.oriMessage = oriMessage;
    }

    void addFollowig(String email){
        this.oriFollowig = this.oriFollowig + "," + email;
    }

    void addMessage(String message){
        this.oriMessage = this.oriMessage + "#####" + message;
    }

}

@Dao
interface UserDao{

    @Query("SELECT * FROM User")
    User[] getAllUsers();

    @Query("SELECT * FROM User WHERE email IN (:email)")
    User getUserByEmail(String... email);

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
