package com.example.newstoday;

import java.io.Serializable;
import java.util.*;
import java.text.*;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class News implements Serializable {
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
    private boolean watched;
    private boolean starred;

//    private Bitmap image;

    private String oriImage;
    private String oriKeywords;

    @Ignore
    private String[] image;
    @Ignore
    private String[] keywords;


    News(final String title, final String date, final String content, final String category, final String organization,
         final String newsID, final String oriImage, final String publisher, final String person, final String location,
         final String oriKeywords, final String url){
        this.title = title;
        this.content = content;
        this.person = person;
        this.organization = organization;
        this.location = location;
        this.category = category;
        this.newsID = newsID;
        this.date = date;
        this.oriImage = oriImage;
        this.publisher = publisher;
        this.watched = false;
        this.starred = false;
        this.url = url;
        this.oriKeywords = oriKeywords;
        this.keywords =stringParse(oriKeywords);

        /*
         * Change string into date
         * */
//        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            this.date = ft.parse(date);
//        } catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
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

    public String getOriImage() {
        return oriImage;
    }

    public String[] getImage(){
        return image;
    }

    public String getPublisher() {
        return publisher;
    }

    public boolean getWatched(){
        return watched;
    }

    public boolean getStarred(){
        return starred;
    }

    public String getUrl(){
        return url;
    }

    public String getOriKeywords(){
        return oriKeywords;
    }

    public String[] getKeywords(){
        return keywords;
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

    public void setOriImage(String oriImage){
        this.oriImage = oriImage;
    }

    public void setImage(String[] image){
        this.image = image;
    }

    public void setPublisher(String publisher){
        this.publisher = publisher;
    }

    public void setWatched(boolean watched){
        this.watched = watched;
    }

    public void setStarred(boolean starred){
        this.starred = starred;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void setOriKeywords(String oriKeywords){
        this.oriKeywords = oriKeywords;
    }

    public void setKeywords(String[] keywords){
        this.keywords = keywords;
    }

}
