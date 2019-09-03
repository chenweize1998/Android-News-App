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
                            oriImage, publisher, person, location, oriKeywords, oriScores, url, video));
                } else if (newsType.equals("collection")) {
                    newsManager.addInCollection(new News(title, date, content, category, organization, newsID,
                            oriImage, publisher, person, location, oriKeywords, oriScores, url, video));
                } else if (newsType.equals("map")) {
                    mapData = news.getString("weight");
                    filterWords = news.getString("filterWords");
                } else if(newsType.equals("forwardingNews")){
                    forwordingNewsManager.addOneForwardingNewsForUser(new News(title, date, content, category, organization, newsID,
                            oriImage, publisher, person, location, oriKeywords, oriScores, url, video), publisher);
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
                userMessageManager.addOneUserMessage(new UserMessage(messageID, email, content, bytes));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean asyncNewsToServer() {
        String oriUrl = "http://166.111.5.239:8000/postAllNews/";

        String newsID = "";
        String title = "";
        String date = "";
        String content = "";
        String person = "";
        String organization = "";
        String location = "";
        String category = "";
        String publisher = "";
        String url = "";
        String oriImage = "";
        String oriKeywords = "";
        String oriScores = "";
        String video = "";
        String newsType = "";
        String mapData = "";
        String filterWords = "";

        ArrayList<News> allHistoryNewsNews = newsManager.getAllHistoryNews();
        for (News news : allHistoryNewsNews) {
            newsID += news.getNewsID() + ",";
            title += news.getTitle() + ",";
            date += news.getDate() + ",";
            content += news.getContent() + ",";
            person += news.getPerson() + ",";
            organization += news.getOrganization() + ",";
            location += news.getLocation() + ",";
            category += news.getCategory() + ",";
            publisher += news.getPublisher() + ",";
            url += news.getUrl() + ",";
            oriImage += news.getOriImage() + ",";
            oriKeywords += news.getOriKeywords() + ",";
            oriScores += news.getOriScores() + ",";
            video += news.getVideo() + ",";
            newsType += "history" + ",";
            mapData += "null" + ",";
            filterWords += "null" + ",";

        }

        ArrayList<News> allCollectionNews = newsManager.getAllCollectionNews();
        for(News news : allCollectionNews){
            newsID += news.getNewsID() + ",";
            title += news.getTitle() + ",";
            date += news.getDate() + ",";
            content += news.getContent() + ",";
            person += news.getPerson() + ",";
            organization += news.getOrganization() + ",";
            location += news.getLocation() + ",";
            category += news.getCategory() + ",";
            publisher += news.getPublisher() + ",";
            url += news.getUrl() + ",";
            oriImage += news.getOriImage() + ",";
            oriKeywords += news.getOriKeywords() + ",";
            oriScores += news.getOriScores() + ",";
            video += news.getVideo() + ",";
            newsType += "collection" + ",";
            mapData += "null" + ",";
            filterWords += "null" + ",";
        }

        ArrayList<News> allForwardingNews = forwordingNewsManager.getAllForwardingNews();
        for(News news: allForwardingNews){
            newsID += news.getNewsID() + ",";
            title += news.getTitle() + ",";
            date += news.getDate() + ",";
            content += news.getContent() + ",";
            person += news.getPerson() + ",";
            organization += news.getOrganization() + ",";
            location += news.getLocation() + ",";
            category += news.getCategory() + ",";
            publisher += news.getPublisher() + ",";
            url += news.getUrl() + ",";
            oriImage += news.getOriImage() + ",";
            oriKeywords += news.getOriKeywords() + ",";
            oriScores += news.getOriScores() + ",";
            video += news.getVideo() + ",";
            newsType += "forwardingNews" + ",";
            mapData += "null" + ",";
            filterWords += "null" + ",";
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

        newsID += "null";
        title += "null";
        date += "null";
        content += "null";
        person += "null";
        organization += "null";
        location += "null";
        category += "null";
        publisher += "null";
        url += "null";
        oriImage += "null";
        oriKeywords += "null";
        oriScores += "null";
        video += "null";
        newsType += "map";
        mapData += weightData;
        filterWords += filterWord;



        String data = "newsID=" + newsID + "&title=" + title + "&date=" + date + "&content=" + content +
                "&person=" + person + "&organization=" + organization + "&location=" + location +
                "&category=" + category + "&publisher=" + publisher + "&url=" + url +
                "&oriImage=" + oriImage + "&oriKeywords=" + oriKeywords +
                "&oriScores=" + oriScores + "&video=" + video +
                "&newsType="+newsType + "&mapData="+mapData +
                "&filterWords=" + filterWords;
        String res = serverHttpResponse.postResponse(oriUrl, data);
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
            String image = new String(userMessage.getImage());
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








