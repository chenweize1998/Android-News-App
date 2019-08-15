package com.example.newstoday;

import com.example.newstoday.News;
import com.example.newstoday.JsonDataFromUrl;
import com.example.newstoday.News.*;


import org.json.*;
import java.io.*;
import java.net.ConnectException;
import java.text.*;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.net.URL;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.HttpURLConnection;

public class NewsManager {

    private int newNewsCounter;
    private static NewsManager Instance = null;
    private  static NewsRepository historyNews;
    private  static NewsRepository collectionNews;


    private NewsManager(Context context){
        newNewsCounter = 0;
        historyNews = new NewsRepository(AppDB.getAppDB(context, "history"));
        collectionNews = new NewsRepository(AppDB.getAppDB(context, "collection"));
    }

    public static NewsManager getNewsManager(Context context){
        if(Instance == null){
            Instance = new NewsManager(context);
        }
        return Instance;
    }

    public News[] getNews(int size, final String startDate, final String endDate, final String words, final String categories) {

        newNewsCounter = 0;
        JsonDataFromUrl jsonData = new JsonDataFromUrl();

        /*
         * Parse json data and construct news object
         * */
        try {
            JSONObject json = jsonData.execute(String.valueOf(size), startDate, endDate, words, categories).get();
            if(Integer.parseInt(json.getString("pageSize")) == 0) {
                return null;
            }

            JSONArray newsArray = json.getJSONArray("data");

            News[] newNews = new News[newsArray.length()];
            for(int i = 0; i<newsArray.length(); i++) {
                try {
                    JSONObject news = newsArray.getJSONObject(i);
                    String title = news.getString("title");
                    String date = news.getString("publishTime");
                    String content = news.getString("content");
                    String category = news.getString("category");
                    String image = news.getString("image");
                    String newsID = news.getString("newsID");
                    String publisher = news.getString("publisher");
//                    String organization = news.getJSONArray("organizations").getJSONObject(0).getString("mention");
                    String organization = "";
                    StringBuffer keywords = new StringBuffer();
                    JSONArray keywordsArray = news.getJSONArray("keywords");
                    for(int j = 0; j<keywordsArray.length();j++){
                        JSONObject keywordsObject = keywordsArray.getJSONObject(i);
                        keywords.append(keywordsObject.getString("word"));
                        if(j!=keywordsArray.length()-1){
                            keywords.append(",");
                        }
                    }

//                    Bitmap bimage = new DownLoadImageTask().execute(image).get();
                    String[] images = image.split(",");
                    for(int j = 0; j < images.length; ++j)
                        images[j] = images[j].replace("[", "").replace("]", "").trim();

                    newNews[newNewsCounter] = new News(title, date, content, category, organization, newsID,
                                                        News.stringConverter(images), publisher, null,
                                                        null, keywords.toString());
                    newNews[newNewsCounter].setImage(images);
                    newNewsCounter++;
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            return newNews;
        }catch(JSONException e){
            e.printStackTrace();
        }catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public News[] getHistoryNews(){
        return historyNews.getAllNews();
    }

    public News[] getCollectionNews(){
        return collectionNews.getAllNews();
    }

    public void addInHistory(News... news){
        historyNews.insertNews(news);
    }

    public void addInCollection(News... news){
        collectionNews.insertNews(news);
    }

    public void deleteOneHistory(News... news){
        historyNews.deleteNews(news);
    }

    public void deleteAllHistory(){
        historyNews.deleteNews(historyNews.getAllNews());
    }

    public void deletaOneCollection(News... news){
        collectionNews.deleteNews(news);
    }

    public int getNewNewsCounter() {
        return newNewsCounter;
    }

}


