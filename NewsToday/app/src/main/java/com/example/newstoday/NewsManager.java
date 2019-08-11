package com.example.newstoday;

import com.example.newstoday.News;
import com.example.newstoday.JsonDataFromUrl;

import org.json.*;
import java.io.*;
import java.text.*;

public class NewsManager {

    private int newNewsCounter;

    NewsManager(){
        newNewsCounter = 0;
    }

    public News[] getNews(int size, final String startDate, final String endDate, final String words, final String categories) {

        newNewsCounter = 0;
        JSONObject json = JsonDataFromUrl.getJsonData(String.valueOf(size), startDate, endDate, words, categories) ;
        /*
         * Parse json data and construct news object
         * */
        try {
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
                    String organization = news.getJSONArray("organizations").getJSONObject(0).getString("mention");

                    newNews[newNewsCounter] = new News(title, date, content, category, organization, null, null);
                    newNewsCounter++;
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }

            return newNews;
        }catch(JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    public int getNewNewsCounter() {
        return newNewsCounter;
    }

//    public static void main(String[] args) throws FileNotFoundException{
//        NewsManager newsManager = new NewsManager();
//        News[] news = newsManager.getNews(10, null, null, "清华大学", "科技");
//        int newsCounter = newsManager.getNewNewsCounter();
//
//        File file = new File("news");
//        BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
//
//        try {
//            bw.write(newsCounter + "\n\r");
//            bw.write(news[0].getTitle());
//            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss" + "\n\r");
//            bw.write(ft.format(news[0].getDate()));
//            bw.write(news[0].getContent() + "\n\r");
//            bw.write(news[0].getCategory() + "\n\r");
//            bw.write(news[0].getOrganization() + "\n\r");
//            bw.close();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
}
