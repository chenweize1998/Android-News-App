package com.example.newstoday;


import org.json.*;

import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.util.ArrayMap;
import android.util.ArraySet;

import java.util.ArrayList;

public class NewsManager {

    private int newNewsCounter;
    private static int pageCounter;
    private static NewsManager Instance = null;
    private static NewsRepository historyNews;
    private static NewsRepository collectionNews;
    private static String lastCategory;
    private ArraySet<String> historyNewsInMem;
    private ArraySet<String> collectionNewsInmem;

    private NewsManager(Context context){
        newNewsCounter = 0;
        pageCounter = 1;
        lastCategory = "娱乐";
        historyNews = new NewsRepository(AppDB.getAppDB(context, "history"));
        collectionNews = new NewsRepository(AppDB.getAppDB(context, "collection"));

        initNewInMem();
    }

    private void initNewInMem(){
        String[] historyNewsID = getHistoryNewsID();
        historyNewsInMem = new ArraySet(historyNewsID.length);
        for(String ID: historyNewsID){
            historyNewsInMem.add(ID);
        }

        String[] collectionNewsID = getCollectionNewsID();
        collectionNewsInmem = new ArraySet(collectionNewsID.length);
        for(String ID: collectionNewsID){
            collectionNewsInmem.add(ID);
        }
    }

    public static NewsManager getNewsManager(Context context){
        if(Instance == null){
            Instance = new NewsManager(context);
        }
        return Instance;
    }

    public ArrayList<News> getNews(int size, final String startDate, final String endDate, final String words, final String categories, boolean refresh, boolean reset) {

        newNewsCounter = 0;
        JsonDataFromUrl jsonData = new JsonDataFromUrl();

        /*
         * Parse json data and construct news object
         * */
        try {
//            if(!lastCategory.equals(categories))
            if(reset || !lastCategory.equals(categories))
                pageCounter = 1;
            JSONObject json;
            json = jsonData.execute(String.valueOf(size), startDate, endDate, words, categories, Integer.toString(pageCounter)).get();
//            if(categories != null)

            if(refresh || !lastCategory.equals(categories)) {
                ++pageCounter;
            }
            lastCategory = categories;
            if(Integer.parseInt(json.getString("pageSize")) == 0) {
                return null;
            }


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

    public ArrayList<News> getAllHistoryNews(){
        return historyNews.getAllNews();
    }

    public ArrayList<News> getAllCollectionNews(){
        return collectionNews.getAllNews();
    }

    private String[] getHistoryNewsID(){return historyNews.getAllNewsID();}

    private String[] getCollectionNewsID(){return collectionNews.getAllNewsID();}

    public void addInHistory(News... news){
        historyNews.insertNews(news);
        for(News _news: news){
            historyNewsInMem.add(_news.getNewsID());
        }
    }

    public void addInCollection(News... news){
        collectionNews.insertNews(news);
        for(News _news: news){
            collectionNewsInmem.add(_news.getNewsID());
        }
    }

    public void deleteOneHistory(News... news){
        historyNews.deleteNews(news);
        for(News _news: news){
            historyNewsInMem.remove(_news.getNewsID());
        }
    }

    public void deleteAllHistory(){
        historyNews.deleteNews((News [])historyNews.getAllNews().toArray());
        historyNewsInMem.clear();
    }

    public void deleteOneCollection(News... news){
        collectionNews.deleteNews(news);
        for(News _news: news){
            collectionNewsInmem.remove(_news.getNewsID());
        }
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

    public boolean inHistoryNews(News news){
        return historyNewsInMem.contains(news.getNewsID());
    }

    public boolean inCollectionNews(News news){
        return collectionNewsInmem.contains(news.getNewsID());
    }

}


