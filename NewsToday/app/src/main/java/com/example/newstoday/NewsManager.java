package com.example.newstoday;


import org.json.*;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import java.util.ArrayList;

public class NewsManager {

    private int newNewsCounter;
    private static int pageCounter;
    private static NewsManager Instance = null;
    private static NewsRepository historyNews;
    private static NewsRepository collectionNews;
    private static String lastCategory;

    private NewsManager(Context context){
        newNewsCounter = 0;
        pageCounter = 1;
        lastCategory = "娱乐";
        historyNews = new NewsRepository(AppDB.getAppDB(context, "history"));
        collectionNews = new NewsRepository(AppDB.getAppDB(context, "collection"));
    }

    public static NewsManager getNewsManager(Context context){
        if(Instance == null){
            Instance = new NewsManager(context);
        }
        return Instance;
    }

    public ArrayList<News> getNews(int size, final String startDate, final String endDate, final String words, final String categories, boolean refresh) {

        newNewsCounter = 0;
        JsonDataFromUrl jsonData = new JsonDataFromUrl();

        /*
         * Parse json data and construct news object
         * */
        try {
            if(!lastCategory.equals(categories))
                pageCounter = 1;
            JSONObject json;
            json = jsonData.execute(String.valueOf(size), startDate, endDate, words, categories, Integer.toString(pageCounter)).get();
            lastCategory = categories;

            if(Integer.parseInt(json.getString("pageSize")) == 0) {
                return null;
            }
            if(refresh)
                ++pageCounter;


            JSONArray newsArray = json.getJSONArray("data");

//            News[] newNews = new News[newsArray.length()];
            ArrayList<News> newNews = new ArrayList<>();
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
                    String url = news.getString("url");
//                    String organization = news.getJSONArray("organizations").getJSONObject(0).getString("mention");
                    String organization = "";
                    StringBuffer keywords = new StringBuffer();
                    JSONArray keywordsArray = news.getJSONArray("keywords");
                    for(int j = 0; j<keywordsArray.length();j++){
                        JSONObject keywordsObject = keywordsArray.getJSONObject(j);
                        keywords.append(keywordsObject.getString("word"));
                        if(j!=keywordsArray.length()-1){
                            keywords.append(",");
                        }
                    }
                    if(keywords.toString().equals("")){
                        keywords.append(category);
                    }

//                    Bitmap bimage = new DownLoadImageTask().execute(image).get();
                    String[] images = image.split(",");
                    for(int j = 0; j < images.length; ++j)
                        images[j] = images[j].replace("[", "").replace("]", "").trim();

//                    newNews[newNewsCounter] = new News(title, date, content, category, organization, newsID,
//                                                        News.stringConverter(images), publisher, null,
//                                                        null, keywords.toString());
//                    newNews[newNewsCounter].setImage(images);
                    newNews.add(new News(title, date, content, category, organization, newsID,
                                                        News.stringConverter(images), publisher, null,
                                                        null, keywords.toString(), url));
                    newNews.get(newNewsCounter).setImage(images);
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

    public ArrayList<News> getHistoryNews(){
        return historyNews.getAllNews();
    }

    public ArrayList<News> getAllCollectionNews(){
        return collectionNews.getAllNews();
    }

    public News getOneCollectionNews(String ID){
        return collectionNews.getOneNews(ID);
    }

    public News getOneHistoryNews(String ID){
        return historyNews.getOneNews(ID);
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
        historyNews.deleteNews((News [])historyNews.getAllNews().toArray());
    }

    public void deleteOneCollection(News... news){
        collectionNews.deleteNews(news);
    }

    public int getNewNewsCounter() {
        return newNewsCounter;
    }

    public String getLastCategory(){
        return lastCategory;
    }

    public int getPageCounter(){
        return pageCounter;
    }

    public void resetPageCounter(){
        pageCounter = 1;
    }

}


