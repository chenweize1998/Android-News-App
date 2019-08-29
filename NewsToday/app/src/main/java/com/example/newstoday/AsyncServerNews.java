package com.example.newstoday;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static java.net.Proxy.Type.HTTP;

public class AsyncServerNews {

    private NewsManager newsManager;

    public AsyncServerNews(Context context){
        newsManager = NewsManager.getNewsManager(context);
    }


    public boolean asyncHistoryNewsFromServer(){
        JSONObject jsonData = getJsonDataFromServer("http://183.172.218.1:8000/history");
//      if jsonData is null, just return false
        if(jsonData == null){
            return false;
        }

        try {
            JSONArray newsArray = jsonData.getJSONArray("data");
            for(int i = 0; i<newsArray.length(); i++){
                JSONObject news = newsArray.getJSONObject(i);
                String newsID = news.getString("newsID");
                String title = news.getString("title");
                String date = news.getString("date");
                String content = news.getString("content");
                String person = news.getString("person");
                String origanization = news.getString("origanization");
                String location = news.getString("location");
                String category = news.getString("category");
                String publisher = news.getString("publisher");
                String url = news.getString("url");
                String oriImage = news.getString("oriImage");
                String oriKeywords = news.getString("oriKeywords");
                String oriScores = news.getString("oriSocres");
                String video = news.getString("video");

                newsManager.addInHistory(new News(title, date, content, category, origanization, newsID,
                                                    oriImage, publisher, person, location, oriKeywords, oriScores, url, video));

                return true;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean asyncCollectionNewsFromServer(){
        JSONObject jsonData = getJsonDataFromServer("http://183.172.218.1:8000/collection");
//      if jsonData is null, just return false
        if(jsonData == null){
            return false;
        }

        try {
            JSONArray newsArray = jsonData.getJSONArray("data");
            for(int i = 0; i<newsArray.length(); i++){
                JSONObject news = newsArray.getJSONObject(i);
                String newsID = news.getString("newsID");
                String title = news.getString("title");
                String date = news.getString("date");
                String content = news.getString("content");
                String person = news.getString("person");
                String origanization = news.getString("origanization");
                String location = news.getString("location");
                String category = news.getString("category");
                String publisher = news.getString("publisher");
                String url = news.getString("url");
                String oriImage = news.getString("oriImage");
                String oriKeywords = news.getString("oriKeywords");
                String oriScores = news.getString("oriSocres");
                String video = news.getString("video");

                newsManager.addInCollection(new News(title, date, content, category, origanization, newsID,
                        oriImage, publisher, person, location, oriKeywords, oriScores, url, video));

                return true;
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;

    }

    public boolean asyncHistoryNewsToServer(){
        ArrayList<News> allHistoryNewsNews = newsManager.getAllHistoryNews();
        String url = "http://183.172.218.1:8000/history";
        for(News news:allHistoryNewsNews){
            String res = postNewsToServer(url, news);
            if(res==null){
                return false;
            }else{
                if(res.equals("Fail")){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean asyncCollectionNewsToServer(){
        ArrayList<News> allCollectionNews = newsManager.getAllCollectionNews();
        String url = "http://183.172.218.1:8000/collection";
        for(News news:allCollectionNews){
            String res = postNewsToServer(url, news);
            if(res==null){
                return false;
            }else{
                if(res.equals("Fail")){
                    return false;
                }
            }
        }
        return true;

    }

    private JSONObject getJsonDataFromServer(String url){
        try {
            GetHttpResponseTask getHttpResponseTask = new GetHttpResponseTask();
            String json = getHttpResponseTask.execute(url).get();
            return new JSONObject(json);
        }catch (JSONException e){
            e.printStackTrace();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }


    private String postNewsToServer(String oriUrl, News news){
        try{
            PostHttpResponseTask postHttpResponseTask = new PostHttpResponseTask();
            return postHttpResponseTask.execute(oriUrl, news).get();
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;

    }

    private String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {

            URL url = new URL(allConfigUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();

            result = new StringBuffer();
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return null;

    }

    private class GetHttpResponseTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... url){
            return getHttpResponse(url[0]);
        }
    }

    private String postHttpResponse(String oriUrl, News news){

        String data = "newsID="+news.getNewsID()+"&tile="+news.getTitle()+"&date="+news.getDate()+
                "&content="+news.getContent()+"&person="+news.getPerson()+"&origanization="+news.getOrganization()+
                "&location="+news.getLocation()+"&category="+news.getCategory()+"&publisher="+news.getPublisher()+
                "&url="+news.getUrl()+"&oriImage="+news.getOriImage()+"&oriKeywords="+news.getOriKeywords()+"&oriScores="+news.getOriScores();
        try{

            URL url = new URL(oriUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("encoding", "UTF-8");//添加请求属性
            connection.setDoInput(true);//允许输入
            connection.setDoOutput(true);//允许输出
            connection.setRequestMethod("POST");//POST请求 要在获取输入输出流之前设置  否则报错


            //输出
            OutputStream os;
            os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(data);
            bw.flush();

            //输入
            InputStream in = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in,"UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            bw.close();
            osw.close();
            os.close();
            br.close();
            isr.close();
            in.close();

            System.out.println(sb.toString());
            return sb.toString();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    private class PostHttpResponseTask extends AsyncTask<Object, Void, String>{
        @Override
        protected String doInBackground(Object... objects){
            return postHttpResponse((String)objects[0], (News)objects[1]);
        }
    }

}


