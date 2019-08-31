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

    public boolean asyncNewsFromServer(){
        try{
            String json = serverHttpResponse.getResponse("http://166.111.5.239:8000/getAllNews/");
            if(json == null || json.equals("Fail")){
                return false;
            }

            JSONObject jsonData = new JSONObject(json);
            String mapData = "";
            String filterWords = "";

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
                String newsType = news.getString("newsType");


                if(newsType.equals("history")) {
                    newsManager.addInHistory(new News(title, date, content, category, organization, newsID,
                            oriImage, publisher, person, location, oriKeywords, oriScores, url, video));
                }else if(newsType.equals("collection")){
                    newsManager.addInCollection(new News(title, date, content, category, organization, newsID,
                            oriImage, publisher, person, location, oriKeywords, oriScores, url, video));
                }else if(newsType.equals("weight")) {
                    mapData = news.getString("weight");
                    filterWords = news.getString("filterWords");
                }
            }

            /**
             * 得到推荐的参数
             * */
            TreeMap<Double, String> map = new TreeMap<>();
            String[] entries = mapData.split(" ");
            for(String entry:entries){
                double weight = Double.parseDouble(entry.split(",")[0]);
                String newsID = entry.split(",")[1];
                map.put(weight, newsID);
            }
            newsManager.setMap(map);

            /**
             * 得到屏蔽的词语
             * */
            TreeMap<String, String> filterWordsMap = new TreeMap<>();
            String[] filterWordEntries = filterWords.split(" ");
            for(String entry:filterWordEntries){
                String weight = entry.split(",")[0];
                String newsID = entry.split(",")[1];
                filterWordsMap.put(weight, newsID);
            }
            newsManager.setFilterWords(filterWordsMap);

            return true;

        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean asyncHistoryNewsToServer(){
        ArrayList<News> allHistoryNewsNews = newsManager.getAllHistoryNews();
        String url = "http://166.111.5.239:8000/history/";
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
        String url = "http://166.111.5.239:8000/collection/";
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
        if(map.size() == 0){
            return true;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("data=");
        Iterator iter = map.keySet().iterator();
        while(iter.hasNext()){
            Double key = (Double) iter.next();
            String value = (String)map.get(key);
            sb.append(key.toString() + "," +value +" ");
        }
        String data = sb.toString();
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/weightMap/", data);
        if(res == null){
            return false;
        }else{
            if(res.equals("Fail")){
                return false;
            }
        }
        return true;
    }

    public boolean asyncFilterWordsToServer(){
        TreeMap<String, String> treeMap = newsManager.getFilterWordsForServer();
        if(treeMap.size() == 0){
            return true;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("data=");
        Iterator iter = treeMap.keySet().iterator();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = treeMap.get(key);
            sb.append(key + "," +value +" ");
        }
        String data = sb.toString();
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/filterWordsMap/", data);
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


