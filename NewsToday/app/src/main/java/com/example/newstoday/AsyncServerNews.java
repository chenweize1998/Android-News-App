package com.example.newstoday;

import android.content.Context;
import android.se.omapi.SEService;
import android.util.ArraySet;

import com.example.newstoday.Activity.Table;

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
    private UserManager userManager;
    private UserMessageManager userMessageManager;
    private static AsyncServerNews INSTANCE = null;


    private AsyncServerNews(Context context) {
        newsManager = NewsManager.getNewsManager(context);
        forwordingNewsManager = ForwordingNewsManager.getForwordingNewsManager(context);
        userManager = UserManager.getUserManager(context);
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
        return (asyncNewsFromServer() && asyncUserFromServer());
    }

    public boolean asyncDataToServer(){
        return (asyncNewsToServer());
    }

    public boolean asyncNewsFromServer() {
        try {
            String json = serverHttpResponse.getResponse("http://166.111.5.239:8000/getAllNews/?user="+Table.header.getActiveProfile().getEmail().toString());
            if (json == null || json.equals("Fail")) {
                return false;
            }

            JSONObject jsonData = new JSONObject(json);
            String mapData = null;
            String filterWords = null;

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
                String emails = news.getString("emails");
                String comments = news.getString("comments");
                String video = news.getString("video");
                String newsType = news.getString("newsType");



                if (newsType.equals("history")) {
                    newsManager.addInHistory(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video,
                            ListConverter.fromTimestamp(emails), ListConverter.fromTimestamp(comments)));
                } else if (newsType.equals("collection")) {
                    newsManager.addInCollection(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video,
                            ListConverter.fromTimestamp(emails), ListConverter.fromTimestamp(comments)));
                } else if (newsType.equals("map")) {
                    mapData = news.getString("weight");
                    filterWords = news.getString("filterWords");
                } else if(newsType.equals("forwardingNews")){
                    forwordingNewsManager.addOneForwardingNewsForUser(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video,
                            ListConverter.fromTimestamp(emails), ListConverter.fromTimestamp(comments)), publisher);
                }else if(newsType.equals("userMessage")){
                    userMessageManager.addOneUserMessage(new News(title, date, content, category, organization, newsID,
                            Converter.fromTimestamp(oriImage), publisher, person, location,
                            Converter.fromTimestamp(oriKeywords), Converter.fromTimestamp(oriScores), url, video,
                            ListConverter.fromTimestamp(emails), ListConverter.fromTimestamp(comments)));
                }
            }

            /**
             * 得到推荐的参数
             * */
            System.out.println(mapData);
            System.out.println(filterWords);
            if(mapData != null && !mapData.equals("null")){
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
            if(filterWords != null && !filterWords.equals("null")) {
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

    public boolean asyncUserFromServer() {
        try {
            String json = serverHttpResponse.getResponse("http://166.111.5.239:8000/user/");
            if (json == null || json.equals("Fail")) {
                return false;
            }

            JSONObject jsonData = new JSONObject(json);

            JSONArray messageArray = jsonData.getJSONArray("data");
            System.out.println("来了" + messageArray.length() + "条用户数据");
            for (int i = 0; i < messageArray.length(); i++) {
                JSONObject message = messageArray.getJSONObject(i);
                String email = message.getString("email");
                String name = message.getString("name");
                String password = message.getString("password");
                String avatar = message.getString("avatar");
                String followig = message.getString("followig");

                if (followig != "null") {
                    ArraySet<String> as = new ArraySet<>();
                    String[] followigs = followig.split(",");
                    for (String _followig : followigs) {
                        as.add(_followig);
                    }
                    userManager.addInUser(new User(email, name, password, as, avatar));
                }else{
                    userManager.addInUser(new User(email, name, password, null, avatar));
                }
            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean asyncNewsToServer() {
        String oriUrl = "http://166.111.5.239:8000/postAllNews/?user="+ Table.header.getActiveProfile().getEmail().toString();
        JSONObject data = new JSONObject();
        JSONArray  dataArray = new JSONArray();

        try {
            ArrayList<News> allHistoryNewsNews = newsManager.getAllHistoryNews();
            for (News news : allHistoryNewsNews) {
                JSONObject newsData = new JSONObject();
                newsData.put("newsID", news.getNewsID());
                newsData.put("title", news.getTitle());
                newsData.put("date", news.getDate());
                newsData.put("content", news.getContent());
                newsData.put("person", news.getPerson());
                newsData.put("organization", news.getOrganization());
                newsData.put("location", news.getLocation());
                newsData.put("category", news.getCategory());
                newsData.put("publisher", news.getPublisher());
                newsData.put("url", news.getUrl());
                newsData.put("image", Converter.toTimestamp(news.getImage()));
                newsData.put("keywords", Converter.toTimestamp(news.getKeywords()));
                newsData.put("scores", Converter.toTimestamp(news.getScores()));
                newsData.put("emails", ListConverter.toTimestamp(news.getEmails()));
                newsData.put("comments", ListConverter.toTimestamp(news.getComments()));
                newsData.put("newsType", "history");
                newsData.put("mapData", "null");
                newsData.put("filterWords", "null");
                newsData.put("video", news.getVideo());
                dataArray.put(newsData);
            }

            ArrayList<News> allCollectionNews = newsManager.getAllCollectionNews();
            for (News news : allCollectionNews) {
                JSONObject newsData = new JSONObject();
                newsData.put("newsID", news.getNewsID());
                newsData.put("title", news.getTitle());
                newsData.put("date", news.getDate());
                newsData.put("content", news.getContent());
                newsData.put("person", news.getPerson());
                newsData.put("organization", news.getOrganization());
                newsData.put("location", news.getLocation());
                newsData.put("category", news.getCategory());
                newsData.put("publisher", news.getPublisher());
                newsData.put("url", news.getUrl());
                newsData.put("image", Converter.toTimestamp(news.getImage()));
                newsData.put("keywords", Converter.toTimestamp(news.getKeywords()));
                newsData.put("scores", Converter.toTimestamp(news.getScores()));
                newsData.put("emails", ListConverter.toTimestamp(news.getEmails()));
                newsData.put("comments", ListConverter.toTimestamp(news.getComments()));
                newsData.put("newsType", "collection");
                newsData.put("mapData", "null");
                newsData.put("filterWords", "null");
                newsData.put("video", news.getVideo());
                dataArray.put(newsData);
            }

            ArrayList<News> allForwardingNews = forwordingNewsManager.getAllForwardingNews();
            for (News news : allForwardingNews) {
                JSONObject newsData = new JSONObject();
                newsData.put("newsID", news.getNewsID());
                newsData.put("title", news.getTitle());
                newsData.put("date", news.getDate());
                newsData.put("content", news.getContent());
                newsData.put("person", news.getPerson());
                newsData.put("organization", news.getOrganization());
                newsData.put("location", news.getLocation());
                newsData.put("category", news.getCategory());
                newsData.put("publisher", news.getPublisher());
                newsData.put("url", news.getUrl());
                newsData.put("image", Converter.toTimestamp(news.getImage()));
                newsData.put("keywords", Converter.toTimestamp(news.getKeywords()));
                newsData.put("scores", Converter.toTimestamp(news.getScores()));
                newsData.put("emails", ListConverter.toTimestamp(news.getEmails()));
                newsData.put("comments", ListConverter.toTimestamp(news.getComments()));
                newsData.put("newsType", "forwardingNews");
                newsData.put("mapData", "null");
                newsData.put("filterWords", "null");
                newsData.put("video", news.getVideo());
                dataArray.put(newsData);
            }

            ArrayList<News> allUserMessage = userMessageManager.getAllUserMessage();
            for (News news : allUserMessage) {
                JSONObject newsData = new JSONObject();
                newsData.put("newsID", news.getNewsID());
                newsData.put("title", news.getTitle());
                newsData.put("date", news.getDate());
                newsData.put("content", news.getContent());
                newsData.put("person", news.getPerson());
                newsData.put("organization", news.getOrganization());
                newsData.put("location", news.getLocation());
                newsData.put("category", news.getCategory());
                newsData.put("publisher", news.getPublisher());
                newsData.put("url", news.getUrl());
                newsData.put("image", Converter.toTimestamp(news.getImage()));
                newsData.put("keywords", Converter.toTimestamp(news.getKeywords()));
                newsData.put("scores", Converter.toTimestamp(news.getScores()));
                newsData.put("emails", ListConverter.toTimestamp(news.getEmails()));
                newsData.put("comments", ListConverter.toTimestamp(news.getComments()));
                newsData.put("newsType", "userMessage");
                newsData.put("mapData", "null");
                newsData.put("filterWords", "null");
                newsData.put("video", news.getVideo());
                dataArray.put(newsData);
            }

            String weightData = "null";
            NavigableMap<Double, String> map = newsManager.getMap();
            if (map != null && map.size() != 0) {
                StringBuffer sb = new StringBuffer();
                Iterator iter = map.keySet().iterator();
                while (iter.hasNext()) {
                    Double key = (Double) iter.next();
                    String value = (String) map.get(key);
                    sb.append(key.toString());
                    sb.append(":");
                    sb.append(value);
                    sb.append(" ");
                }
                weightData = sb.toString();
            }

            String filterWord = "null";
            TreeMap<String, String> treeMap = newsManager.getFilterWordsForServer();
            if (treeMap != null && treeMap.size() != 0) {
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

            JSONObject newsData = new JSONObject();
            newsData.put("newsID", "null");
            newsData.put("title", "null");
            newsData.put("date", "null");
            newsData.put("content", "null");
            newsData.put("person", "null");
            newsData.put("organization", "null");
            newsData.put("location", "null");
            newsData.put("category", "null");
            newsData.put("publisher", "null");
            newsData.put("url", "null");
            newsData.put("image", "null");
            newsData.put("keywords", "null");
            newsData.put("scores", "null");
            newsData.put("emails", "null");
            newsData.put("comments", "null");
            newsData.put("newsType", "map");
            newsData.put("mapData", weightData);
            newsData.put("filterWords", filterWord);
            newsData.put("video", "null");
            dataArray.put(newsData);

            data.put("data", dataArray);

            String res = serverHttpResponse.postResponse(oriUrl, data.toString());
            if (res == null) {
                return false;
            } else {
                if (res.equals("Fail")) {
                    return false;
                }
            }
            return true;
        }catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 这个函数被 *单独* 拎出来调用的时候，只有修改头像和修改关注者之后
     * 这个函数只能被单独拎出来调用
     */
    public boolean asyncUserToServer(User user) {
//        User user = null;
        String url = "http://166.111.5.239:8000/user/";

        StringBuilder sb = new StringBuilder();
        sb.append("email=");
        sb.append(user.getEmail());
        sb.append("&name=");
        sb.append(user.getName());
        sb.append("&password=");
        sb.append(user.getPassword());
        sb.append("&followig=");
        if (user.getFollowig().size() != 0) {
            StringBuilder sber = new StringBuilder();
            ArraySet<String> followigs = user.getFollowig();
            for(String followig:followigs){
                sber.append(followig);
                sber.append(",");
            }
            sber.delete(sber.length()-1, sber.length());
            sb.append(sber);
        }else{
            sb.append("null");
        }
        sb.append("&avatar=");
        sb.append(user.getAvatar());
        System.out.println(user.getAvatar());

        String res = serverHttpResponse.postResponse(url, sb.toString());
        if (res == null) {
            return false;
        } else {
            if (res.equals("Fail")) {
                return false;
            }
        }

        return true;
    }

}








