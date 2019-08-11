package com.example.newstoday;

import com.example.newstoday.News;
import com.example.newstoday.JsonDataFromUrl;

import org.json.*;
import java.io.*;
import java.net.ConnectException;
import java.text.*;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.net.URL;
import android.os.AsyncTask;
import java.net.HttpURLConnection;

public class NewsManager {

    private int newNewsCounter;

    NewsManager(){
        newNewsCounter = 0;
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
//                    String organization = news.getJSONArray("organizations").getJSONObject(0).getString("mention");
                    String organization = "";

                    Bitmap bimage = new DownLoadImageTask().execute(image).get();

                    newNews[newNewsCounter] = new News(title, date, content, category, organization, newsID, bimage, null, null);
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
    class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
    protected Bitmap doInBackground(String...urls){
//        System.out.println(urls[0]);
        String[] urlOfImage = urls[0].split(",");
        String urlText = urlOfImage[0];
        if (urlOfImage.length > 1)
            urlText = urlText.substring(1, urlText.length());
        else
            urlText = urlText.substring(1, urlText.length()-1);
        if(urlText == "")
            return null;
        Bitmap bimage = null;
        try{
            URL url = new URL(urlText);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bimage = BitmapFactory.decodeStream(input);
            return bimage;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println("Error:"+urlText);
        }
        return bimage;
        }
    }
}


