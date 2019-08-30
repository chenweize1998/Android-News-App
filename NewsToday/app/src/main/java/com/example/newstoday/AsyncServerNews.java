package com.example.newstoday;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;


public class AsyncServerNews {

    private NewsManager newsManager;
    private ServerHttpResponse serverHttpResponse;
    private static AsyncServerNews INSTANCE=null;


    private AsyncServerNews(Context context){
        newsManager = NewsManager.getNewsManager(context);
        serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    }

    public static AsyncServerNews getAsyncServerNews(Context context){
        if(INSTANCE == null){
            INSTANCE = new AsyncServerNews(context);
        }
        return INSTANCE;
    }

    public boolean asyncHistoryNewsFromServer(){
        try{
            JSONObject jsonData = new JSONObject(serverHttpResponse.getResponse("http://183.172.218.1:8000/history/"));

            if(jsonData == null){
                return false;
            }
            JSONArray newsArray = jsonData.getJSONArray("data");
            System.out.println("来了"+newsArray.length()+"条数据");
            for(int i = 0; i<newsArray.length(); i++){
                JSONObject news = newsArray.getJSONObject(i);
                String newsID = news.getString("newsID");
                String title = news.getString("title");
                String date = news.getString("date");
                String content = news.getString("content");
                String person = news.getString("person");
                String organization = news.getString("organization");
                String location = news.getString("location");
                String category = news.getString("category");
                String publisher = news.getString("publisher");
                String url = news.getString("url");
                String oriImage = news.getString("oriImage");
                String oriKeywords = news.getString("oriKeywords");
                String oriScores = news.getString("oriScores");
                String video = news.getString("video");

                newsManager.addInHistory(new News(title, date, content, category, organization, newsID,
                                                    oriImage, publisher, person, location, oriKeywords, oriScores, url, video));

            }
            return true;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean asyncCollectionNewsFromServer(){
        try {
            JSONObject jsonData = new JSONObject(serverHttpResponse.getResponse("http://183.172.218.1:8000/collection/"));

            if(jsonData == null){
                return false;
            }


            JSONArray newsArray = jsonData.getJSONArray("data");
            for(int i = 0; i<newsArray.length(); i++){
                JSONObject news = newsArray.getJSONObject(i);
                String newsID = news.getString("newsID");
                String title = news.getString("title");
                String date = news.getString("date");
                String content = news.getString("content");
                String person = news.getString("person");
                String organization = news.getString("organization");
                String location = news.getString("location");
                String category = news.getString("category");
                String publisher = news.getString("publisher");
                String url = news.getString("url");
                String oriImage = news.getString("oriImage");
                String oriKeywords = news.getString("oriKeywords");
                String oriScores = news.getString("oriScores");
                String video = news.getString("video");

                newsManager.addInCollection(new News(title, date, content, category, organization, newsID,
                        oriImage, publisher, person, location, oriKeywords, oriScores, url, video));

            }
            return true;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;

    }

    public boolean asyncWeightMapFromServer(){
        String mapData = serverHttpResponse.getResponse("http://183.172.218.1:8000/weightMap/");
        if(mapData == null){
            return false;
        }else{
            if(mapData.equals("Fail")){
                return false;
            }
        }
        TreeMap<Double, String> map = new TreeMap<>();
        String[] entries = mapData.split(" ");
        for(String entry:entries){
            double weight = Double.parseDouble(entry.split(", ")[0]);
            String newsID = entry.split(", ")[1];
            map.put(weight, newsID);
        }
        newsManager.setMap(map);
        return true;
    }


    public boolean asyncHistoryNewsToServer(){
        ArrayList<News> allHistoryNewsNews = newsManager.getAllHistoryNews();
        String url = "http://183.172.218.1:8000/history/";
        for(News news:allHistoryNewsNews){

            String data = "newsID="+news.getNewsID()+"&title="+news.getTitle()+"&date="+news.getDate()+
                    "&content="+news.getContent()+"&person="+news.getPerson()+"&organization="+news.getOrganization()+
                    "&location="+news.getLocation()+"&category="+news.getCategory()+"&publisher="+news.getPublisher()+
                    "&url="+news.getUrl()+"&oriImage="+news.getOriImage()+"&oriKeywords="+news.getOriKeywords()+
                    "&oriScores="+news.getOriScores()+"&video="+news.getVideo();
            String res = serverHttpResponse.postResponse(url, data);
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
        String url = "http://183.172.218.1:8000/collection/";
        for(News news:allCollectionNews){

            String data = "newsID="+news.getNewsID()+"&title="+news.getTitle()+"&date="+news.getDate()+
                            "&content="+news.getContent()+"&person="+news.getPerson()+"&organization="+news.getOrganization()+
                                "&location="+news.getLocation()+"&category="+news.getCategory()+"&publisher="+news.getPublisher()+
                                    "&url="+news.getUrl()+"&oriImage="+news.getOriImage()+"&oriKeywords="+news.getOriKeywords()+
                                        "&oriScores="+news.getOriScores()+"&video="+news.getVideo();
            String res = serverHttpResponse.postResponse(url, data);
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

    public boolean asyncWeightMapToServer(){
        NavigableMap<Double, String> map= newsManager.getMap();
        StringBuffer sb = new StringBuffer();
        Iterator iter = map.keySet().iterator();
        while(iter.hasNext()){
            Double key = (Double) iter.next();
            String value = (String)map.get(key);
            sb.append(key.toString() + "," +value +" ");
        }
        String data = sb.toString();
        String res = serverHttpResponse.postResponse("http://183.172.218.1:8000/weightMap/", data);
        if(res == null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        return true;
    }

}


