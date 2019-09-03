package com.example.newstoday;

import java.io.Serializable;
import java.util.*;
import java.text.*;

import android.content.Context;
import android.graphics.Bitmap;

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


@Entity
@TypeConverters({Converter.class})
public class News implements Serializable{
    /*
     * These are vital properties of News.
     * All fields are private
     * */
    @NonNull
    @PrimaryKey
    private String newsID;

    private String title;
    private String date;
    private String content;
    private String person;
    private String organization;
    private String location;
    private String category;
    private String publisher;
    private String url;

    private String video;
    @Nullable
    private String[] image;
    @Nullable
    private String[] keywords;
    @Nullable
    private String[] scores;


    News(final String title, final String date, final String content, final String category, final String organization,
         final String newsID, final String[] image, final String publisher, final String person, final String location,
         final String[] keywords, final String[] scores, final String url, final String video){
        this.title = title;
        this.content = content;
        this.person = person;
        this.organization = organization;
        this.location = location;
        this.category = category;
        this.newsID = newsID;
        this.date = date;
        this.image = image;
        this.publisher = publisher;
        this.url = url;
        this.keywords = keywords;
        this.scores = scores;
        this.video = video;
    }

    public static String stringConverter(String[] image){
        StringBuffer sb = new StringBuffer();
        for(String string:image){
            sb.append(string);
            sb.append(',');
        }
        sb.delete(sb.length()-1, sb.length());
        return sb.toString();
    }

    public static String[] stringParse(String image){
        return image.split(",");
    }


    /*getter*/

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getPerson() {
        return person;
    }

    public String getOrganization() {
        return organization;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getNewsID() {
        return newsID;
    }


    public String[] getImage(){
        return image;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getUrl(){
        return url;
    }


    public String[] getKeywords(){
        return keywords;
    }

    public String[] getScores(){
        return scores;
    }

    public Double[] getDoubleScores(){
        Double[] result = new Double[scores.length];
        for(int i = 0; i < scores.length; ++i)
            result[i] = Double.parseDouble(scores[i]);
        return result;
    }


    public String getVideo(){
        return video;
    }

    /*setter*/
    public void setNewsID(String newsID){
        this.newsID = newsID;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setOrganization(String organization){
        this.organization = organization;
    }

    public void setPerson(String person){
        this.person = person;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setImage(String[] image){
        this.image = image;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setKeywords(String[] keywords){
        this.keywords = keywords;
    }

    public void setScores(String[] scores){
        this.scores = scores;
    }

    public void setVideo(String video){
        this.video = video;
    }
}



