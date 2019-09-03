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
    private ForwordingNewsManager forwordingNewsManager;
    private UserMessageManager userMessageManager;
    private static AsyncServerNews INSTANCE = null;


    private AsyncServerNews(Context context) {
        newsManager = NewsManager.getNewsManager(context);
        forwordingNewsManager = ForwordingNewsManager.getForwordingNewsManager(context);
        userMessageManager = UserMessageManager.getUserMessageManager(context);
        serverHttpResponse = ServerHttpResponse.getServerHttpResponse();
    }

    public static AsyncServerNews getAsyncServerNews(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AsyncServerNews(context);
        }
        return INSTANCE;
    }

    public boolean asyncDataFromServer(){
        return (asyncNewsFromServer() && asyncUserMessageFromServer());
    }

    public boolean asyncDataToServer(){
        return (asyncNewsToServer() && asyncUserMessageToServer());
    }

    public boolean deleteUserNewsAndMessageOnServer(String email){
        String data = "email=" + email;
        String res = serverHttpResponse.postResponse("http://166.111.5.239:8000/deleteNewsAndMessage/", data);
        if (res == null || res.equals("Fail")) {
            return false;
        }
        return true;
    }

    public boolean asyncNewsFromServer() {
        try {
            String json = serverHttpResponse.getResponse("http://166.111.5.239:8000/getAllNews/");
            if (json == null || json.equals("Fail")) {
                return false;
            }

            JSONObject jsonData = new JSONObject(json);
            String mapData = "null";
            String filterWords = "null";

            JSONArray newsArray = jsonData.getJSONArray("data");
            System.out.println("来了" + newsArray.length() + "条数据");
            for (int i = 0; i < newsArray.length(); i++) {
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


                if (newsType.equals("history")) {
                    newsManager.addInHistory(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video));
                } else if (newsType.equals("collection")) {
                    newsManager.addInCollection(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video));
                } else if (newsType.equals("map")) {
                    mapData = news.getString("weight");
                    filterWords = news.getString("filterWords");
                } else if(newsType.equals("forwardingNews")){
                    forwordingNewsManager.addOneForwardingNewsForUser(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video), publisher);
                }
            }

            /**
             * 得到推荐的参数
             * */
            System.out.println(mapData);
            System.out.println(filterWords);
            if(!mapData.equals("null")){
                TreeMap<Double, String> map = new TreeMap<>();
                String[] entries = mapData.split(" ");
                for (String entry : entries) {
                    double weight = Double.parseDouble(entry.split(":")[0]);
                    String newsID = entry.split(":")[1];
                    map.put(weight, newsID);
                }
                newsManager.setMap(map);
            }

            /**
             * 得到屏蔽的词语
             * */
            if(!filterWords.equals("null")) {
                TreeMap<String, String> filterWordsMap = new TreeMap<>();
                String[] filterWordEntries = filterWords.split(" ");
                for (String entry : filterWordEntries) {
                    String weight = entry.split(":")[0];
                    String newsID = entry.split(":")[1];
                    filterWordsMap.put(weight, newsID);
                }
                newsManager.setFilterWords(filterWordsMap);
            }

            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean asyncUserMessageFromServer() {
        try {
            String json = serverHttpResponse.getResponse("http://166.111.5.239:8000/userMessage/");
            if (json == null || json.equals("Fail")) {
                return false;
            }

            JSONObject jsonData = new JSONObject(json);

            JSONArray messageArray = jsonData.getJSONArray("data");
            System.out.println("来了" + messageArray.length() + "条用户发布数据");
            for (int i = 0; i < messageArray.length(); i++) {
                JSONObject message = messageArray.getJSONObject(i);
                String messageID = message.getString("messageID");
                String email = message.getString("email");
                String content = message.getString("content");
                String image = message.getString("image");
                byte[] bytes = image.getBytes();
                userMessageManager.addOneUserMessage(new UserMessage(messageID, email, content, ImageConverter.fromTimestamp(bytes)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean asyncNewsToServer() {
        String oriUrl = "http://166.111.5.239:8000/postAllNews/";

        StringBuilder newsID = new StringBuilder();
        StringBuilder title = new StringBuilder();
        StringBuilder date = new StringBuilder();
        StringBuilder content = new StringBuilder();
        StringBuilder person = new StringBuilder();
        StringBuilder organization = new StringBuilder();
        StringBuilder location = new StringBuilder();
        StringBuilder category = new StringBuilder();
        StringBuilder publisher = new StringBuilder();
        StringBuilder url = new StringBuilder();
        StringBuilder image = new StringBuilder();
        StringBuilder keywords = new StringBuilder();
        StringBuilder scores = new StringBuilder();
        StringBuilder video = new StringBuilder();
        StringBuilder newsType = new StringBuilder();
        StringBuilder mapData = new StringBuilder();
        StringBuilder filterWords = new StringBuilder();

        ArrayList<News> allHistoryNewsNews = newsManager.getAllHistoryNews();
        for (News news : allHistoryNewsNews) {
            newsID.append(news.getNewsID());
            newsID.append(",");
            title.append(news.getTitle());
            title.append(",");
            date.append(news.getDate());
            date.append(",");
            content.append(news.getContent());
            content.append(",");
            person.append(news.getPerson());
            person.append(",");
            organization.append(news.getOrganization());
            organization.append(",");
            location.append(news.getLocation());
            location.append(",");
            category.append(news.getCategory());
            category.append(",");
            publisher.append(news.getPublisher());
            publisher.append(",");
            url.append(news.getUrl());
            url.append(",");
            image.append(Converter.toTimestamp(news.getImage()));
            image.append(",");
            keywords.append(Converter.toTimestamp(news.getKeywords()));
            keywords.append(",");
            scores.append(Converter.toTimestamp(news.getScores()));
            scores.append(",");
            video.append(news.getVideo());
            video.append(",");
            newsType.append("history");
            newsType.append(",");
            mapData.append("null");
            mapData.append(",");
            filterWords.append("null");
            filterWords.append(",");
        }

        ArrayList<News> allCollectionNews = newsManager.getAllCollectionNews();
        for(News news : allCollectionNews){
            newsID.append(news.getNewsID());
            newsID.append(",");
            title.append(news.getTitle());
            title.append(",");
            date.append(news.getDate());
            date.append(",");
            content.append(news.getContent());
            content.append(",");
            person.append(news.getPerson());
            person.append(",");
            organization.append(news.getOrganization());
            organization.append(",");
            location.append(news.getLocation());
            location.append(",");
            category.append(news.getCategory());
            category.append(",");
            publisher.append(news.getPublisher());
            publisher.append(",");
            url.append(news.getUrl());
            url.append(",");
            image.append(Converter.toTimestamp(news.getImage()));
            image.append(",");
            keywords.append(Converter.toTimestamp(news.getKeywords()));
            keywords.append(",");
            scores.append(Converter.toTimestamp(news.getScores()));
            scores.append(",");
            video.append(news.getVideo());
            video.append(",");
            newsType.append("collection");
            newsType.append(",");
            mapData.append("null");
            mapData.append(",");
            filterWords.append("null");
            filterWords.append(",");
        }

        ArrayList<News> allForwardingNews = forwordingNewsManager.getAllForwardingNews();
        for(News news: allForwardingNews){
            newsID.append(news.getNewsID());
            newsID.append(",");
            title.append(news.getTitle());
            title.append(",");
            date.append(news.getDate());
            date.append(",");
            content.append(news.getContent());
            content.append(",");
            person.append(news.getPerson());
            person.append(",");
            organization.append(news.getOrganization());
            organization.append(",");
            location.append(news.getLocation());
            location.append(",");
            category.append(news.getCategory());
            category.append(",");
            publisher.append(news.getPublisher());
            publisher.append(",");
            url.append(news.getUrl());
            url.append(",");
            image.append(Converter.toTimestamp(news.getImage()));
            image.append(",");
            keywords.append(Converter.toTimestamp(news.getKeywords()));
            keywords.append(",");
            scores.append(Converter.toTimestamp(news.getScores()));
            scores.append(",");
            video.append(news.getVideo());
            video.append(",");
            newsType.append("forwardingNews");
            newsType.append(",");
            mapData.append("null");
            mapData.append(",");
            filterWords.append("null");
            filterWords.append(",");
        }

        String weightData = "null";
        NavigableMap<Double, String> map = newsManager.getMap();
        if (map.size() != 0) {
            StringBuffer sb = new StringBuffer();
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                Double key = (Double) iter.next();
                String value = (String)map.get(key);
                sb.append(key.toString());
                sb.append(":");
                sb.append(value);
                sb.append(" ");
            }
            weightData = sb.toString();
        }

        String filterWord = "null";
        TreeMap<String, String> treeMap = newsManager.getFilterWordsForServer();
        if (treeMap.size() != 0) {
            StringBuffer sb = new StringBuffer();
            Iterator iter = treeMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = treeMap.get(key);
                sb.append(key);
                sb.append(":");
                sb.append(value);
                sb.append(" ");
            }
            filterWord = sb.toString();
        }

        newsID.append("null");
        title.append("null");
        date.append("null");
        content.append("null");
        person.append("null");
        organization.append("null");
        location.append("null");
        category.append("null");
        publisher.append("null");
        url.append("null");
        image.append("null");
        keywords.append("null");
        scores.append("null");
        video.append("null");
        newsType.append("map");
        mapData.append(weightData);
        filterWords.append(filterWord);



        StringBuilder data = new StringBuilder();
        data.append("newsID=");
        data.append(newsID);
        data.append("&title=");
        data.append(title);
        data.append("&date=");
        data.append(date);
        data.append("&content=");
        data.append(content);
        data.append("&person=");
        data.append(person);
        data.append("&organization=");
        data.append(organization);
        data.append("&location=");
        data.append(location);
        data.append("&category=");
        data.append(category);
        data.append("&publisher=");
        data.append(publisher);
        data.append("&url=");
        data.append(url);
        data.append("&oriImage=");
        data.append(image);
        data.append("&oriKeywords=");
        data.append(keywords);
        data.append("&oriScores=");
        data.append(scores);
        data.append("&video=" );
        data.append(video);
        data.append("&newsType=");
        data.append(newsType);
        data.append("&mapData=");
        data.append(mapData);
        data.append("&filterWords=");
        data.append(filterWords);
        String res = serverHttpResponse.postResponse(oriUrl, data.toString());
        if (res == null) {
            return false;
        } else {
            if (res.equals("Fail")) {
                return false;
            }
        }
        return true;
    }

    public boolean asyncUserMessageToServer() {
        ArrayList<UserMessage> allUserMessage = userMessageManager.getAllUserMessage();
        String url = "http://166.111.5.239:8000/userMessage/";
        for (UserMessage userMessage : allUserMessage) {
            String image = new String(ImageConverter.toTimestamp(userMessage.getImage()));
            String data = "messageID=" + userMessage.getMessageID() + "&email=" + userMessage.getEmail() +
                    "&content=" + userMessage.getContent() + "&image=" + image;
            String res = serverHttpResponse.postResponse(url, data);
            if (res == null) {
                return false;
            } else {
                if (res.equals("Fail")) {
                    return false;
                }
            }
        }
        return true;
    }

}








