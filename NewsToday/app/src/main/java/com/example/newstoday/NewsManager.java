package com.example.newstoday;


import org.json.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.util.ArraySet;

import com.example.newstoday.Activity.NewsItem;
import com.example.newstoday.Activity.Table;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.ArrayList;
import java.util.TreeMap;

import java.util.Collections;

public class NewsManager {

    private int newNewsCounter;
    private static int pageCounter;
    private static NewsManager Instance = null;
    private static NewsRepository historyNews;
    private static NewsRepository collectionNews;
    private static String lastCategory;
    private static RandomCollection<String> recommendKeyword;
    private ArraySet<String> historyNewsInMem;
    private ArraySet<String> collectionNewsInmem;
    private final String allCategory = "娱乐,军事,教育,文化,健康,财经,体育,汽车,科技,社会";
    private final int recommendWordCnt = 4;
    private ArraySet<String> recommended = new ArraySet<>();
    private ArrayMap<String, Integer> keywordPage = new ArrayMap<>();
    private TreeMap<String, String> filterWords = new TreeMap<String, String>();
    private AhoCorasickDoubleArrayTrie<String> acdat = null;
    private ForwordingNewsManager forwordingNewsManager;
    private UserManager userManager;
    private UserMessageManager userMessageManager;
    private OfflineNewsManager offlineNewsManager;

    private NewsManager(Context context){
        newNewsCounter = 0;
        pageCounter = 1;
        lastCategory = "推荐";
        historyNews = new NewsRepository(AppDB.getAppDB(context, "history"));
        collectionNews = new NewsRepository(AppDB.getAppDB(context, "collection"));
        recommendKeyword = new RandomCollection<>();
        forwordingNewsManager = ForwordingNewsManager.getForwordingNewsManager(context);
        userManager = UserManager.getUserManager(context);
        userMessageManager = UserMessageManager.getUserMessageManager(context);
        offlineNewsManager = OfflineNewsManager.getOfflineNewsManager(context);
        initNewInMem();
    }

    public void resetRecommendation(){
        for(String key:keywordPage.keySet())
            keywordPage.put(key, 1);
        recommended.clear();
    }

    public void resetWeightMap(){
        recommendKeyword.reset();
    }

    public void resetCategory(){
        lastCategory = "推荐";
    }

    private void initNewInMem(){
        String[] historyNewsID = getHistoryNewsID();
        historyNewsInMem = new ArraySet();
        historyNewsInMem.addAll(Arrays.asList(historyNewsID));

        String[] collectionNewsID = getCollectionNewsID();
        collectionNewsInmem = new ArraySet();
        collectionNewsInmem.addAll(Arrays.asList(collectionNewsID));
    }

    public static NewsManager getNewsManager(Context context){
        if(Instance == null){
            Instance = new NewsManager(context);
        }
        return Instance;
    }

    public ArrayList<News> getNews(int size, final String startDate, final String endDate, final String words,
                                   final String categories, boolean refresh, boolean reset) {

        newNewsCounter = 0;

        try {
            if(categories.equals("关注")){
                lastCategory = categories;
                NewsItem.refreshLayout.setEnableLoadMore(false);
                if(Table.header.getActiveProfile() != null) {
                    ArrayList<News> forwarding = forwordingNewsManager.getUserAllFollowigNews(userManager.getUserByEmail(
                            Table.header.getActiveProfile().getEmail().toString())[0]);
                    ArrayList<News> publish = userMessageManager.getUserAllFollowigMessage(
                            userManager.getUserByEmail(Table.header.getActiveProfile().getEmail().toString())[0]);
                    publish.addAll(forwarding);
//                    Collections.shuffle(publish);
                    Collections.sort(publish, new Comparator<News>() {
                        @Override
                        public int compare(News news, News t1) {
                            return -news.getDate().compareTo(t1.getDate());
                        }
                    });
                    return publish;
//                    ArrayList<UserMessage> all = userMessageManager.getUserAllFollowigMessage(
//                            userManager.getUserByEmail(Table.header.getActiveProfile().getEmail().toString()
//                    )[0]);
                }
                else
                    return new ArrayList<News>();
            }
            if(reset || !lastCategory.equals(categories))
                pageCounter = 1;
            JSONObject json;
            ArrayList<News> newNews = new ArrayList<>();
            boolean recommendJudge = categories.equals("推荐");
            if(recommendJudge && !lastCategory.equals("推荐"))
                resetRecommendation();
            int cnt = 0;
            String recommendWord = null;
            do {
                JsonDataFromUrl jsonData = new JsonDataFromUrl();
                recommendWord = recommendKeyword.next();
//                System.out.println(recommendWord);
                if(recommendJudge && recommendWord != null) {
                    json = jsonData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf(size / recommendWordCnt), startDate, endDate,
                            recommendWord, allCategory, Integer.toString(keywordPage.getOrDefault(recommendWord, 1))).get();
                    keywordPage.put(recommendWord, keywordPage.getOrDefault(recommendWord, 1) + 1);
//                    System.out.println(keywordPage.get(recommendWord));
                } else {
                    System.out.println("Execute");
                    json = jsonData.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,String.valueOf(size), startDate, endDate, words,
                            recommendJudge ? allCategory : categories, Integer.toString(pageCounter)).get();
                }
                if (((refresh || !lastCategory.equals(categories)) && (!recommendJudge))
                        || (recommendJudge && recommendWord == null) ) {
                    ++pageCounter;  // 推荐的在前面 按词更新
                }
                lastCategory = categories;

                if(json != null){
                    if (Integer.parseInt(json.getString("pageSize")) == 0) {
                        return null;
                    }
                    JSONArray newsArray = json.getJSONArray("data");

                    for (int i = 0; i < newsArray.length(); i++) {
                        try {
                            JSONObject news = newsArray.getJSONObject(i);
                            String newsID = news.getString("newsID");
                            if(recommendJudge && recommended.contains(newsID))
                                continue;
                            String title = news.getString("title");
                            String date = news.getString("publishTime");
                            String content = news.getString("content");
                            String category = news.getString("category");
                            String image = news.getString("image");
                            String publisher = news.getString("publisher");
                            String url = news.getString("url");
                            String video = news.getString("video");
                            //                    String organization = news.getJSONArray("organizations").getJSONObject(0).getString("mention");
                            String organization = "";
                            StringBuffer keywords = new StringBuffer();
                            StringBuffer scores = new StringBuffer();
                            JSONArray keywordsArray = news.getJSONArray("keywords");
                            for (int j = 0; j < keywordsArray.length(); j++) {
                                JSONObject keywordsObject = keywordsArray.getJSONObject(j);
                                String word = keywordsObject.getString("word");
                                String score = keywordsObject.getString("score");
                                keywords.append(word);
                                scores.append(score);
                                if (j != keywordsArray.length() - 1) {
                                    keywords.append("#^#");
                                    scores.append("#^#");
                                }
                            }
                            if (keywords.toString().equals("")) {
                                keywords.append(category);
                                scores.append(0);
                            }

                            //                    Bitmap bimage = new DownLoadImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,image).get();
                            String[] images = image.split(",");
                            for (int j = 0; j < images.length; ++j)
                                images[j] = images[j].replace("[", "").replace("]", "").trim();

                            StringBuilder builder = new StringBuilder();
                            builder.append(title);
                            builder.append(content);
                            if(acdat != null) {
                                List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acdat.parseText(builder.toString());
                                if (wordList.size() != 0)
                                    continue;
                            }

                            newNews.add(new News(title, date, content, category, organization, newsID,
                                    images, publisher, "","",
                                    Converter.fromTimestamp(keywords.toString()),
                                    Converter.fromTimestamp(scores.toString()), url, video, null, null));
                            if(recommendJudge && recommendWord != null)
                                recommended.add(newsID);
                            newNewsCounter++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    ArrayList<News> offlineNews =  offlineNewsManager.getAllOfflineNews();
                    System.out.println("有"+offlineNews.size()+"条本地新闻");
                    for(News news:offlineNews){
                        String newsID = news.getNewsID();
                        if(recommendJudge && recommended.contains(newsID))
                            continue;
                        StringBuilder builder = new StringBuilder();
                        builder.append(news.getTitle());
                        builder.append(news.getContent());
                        if(acdat != null) {
                            List<AhoCorasickDoubleArrayTrie.Hit<String>> wordList = acdat.parseText(builder.toString());
                            if (wordList.size() != 0)
                                continue;
                        }
                        if(recommendJudge && recommendWord != null) {
                            recommended.add(newsID);
                        }
                    }
                    newNewsCounter = offlineNews.size();
                    NewsItem.refreshLayout.setEnableLoadMore(false);
                    NewsItem.refreshLayout.setEnableRefresh(false);
                    return offlineNews;
                }
            } while (recommendJudge && ++cnt < recommendWordCnt && recommendWord != null);
//            if(recommendJudge) {
//                Collections.shuffle(newNews);
//            }
            NewsItem.refreshLayout.setEnableLoadMore(true);
            NewsItem.refreshLayout.setEnableRefresh(true);
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

    public Set<String> getFilterWords(){
        return filterWords.keySet();
    }

    public TreeMap<String, String> getFilterWordsForServer(){
        return filterWords;
    }

    public void setFilterWords(TreeMap<String, String> filterWords){
        this.filterWords = filterWords;
    }

    public void addInHistory(News... news){
        historyNews.insertNews(news);
        for(News _news: news){
            historyNewsInMem.add(_news.getNewsID());
        }
        System.out.println("插入一条历史数据");
    }

    public void addInCollection(News... news) {
        collectionNews.insertNews(news);
        for (News _news : news) {
            collectionNewsInmem.add(_news.getNewsID());
        }
        System.out.println("插入一条收藏数据");
    }
    
    public void addFilterWord(String word){
        filterWords.put(word, word);
    }

    public void deleteOneHistory(News... news){
        historyNews.deleteNews(news);
        for(News _news: news){
            historyNewsInMem.remove(_news.getNewsID());
        }
    }

    public void deleteAllHistory(){
        historyNews.clearNews();
        historyNewsInMem.clear();
    }

    public void deleteOneCollection(News... news){
        collectionNews.deleteNews(news);
        for(News _news: news){
            collectionNewsInmem.remove(_news.getNewsID());
        }
    }

    public void deleteAllCollection(){
        collectionNews.clearNews();
        collectionNewsInmem.clear();
    }


    public void deleteFilterWord(String word){
        filterWords.remove(word);
    }

    public boolean hasFilterWord(String word){
        return filterWords.containsKey(word);
    }

    public void buildFilterWords(){
        if(filterWords.size() == 0)
            acdat = null;
        else {
            acdat = new AhoCorasickDoubleArrayTrie<>();
            acdat.build(filterWords);
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

    /**
     * TODO
     * 更新数据库中的新闻
     * 这个新闻有可能在数据库里，也有可能不在数据库里面
     * 每次更新了内存里面的news之后，就需要调用这个函数更新数据库里的
     * */
    public void updateNews(News news){
        historyNews.updateNews(news);
        collectionNews.updateNews(news);
    }

    public News getNewsByNewsID(String...newsID){
        return historyNews.getNewsByNewsID(newsID);
    }


    public void addWeight(double weight, String result){
        recommendKeyword.add(weight, result);
    }

    public void setMap(NavigableMap<Double, String> map){
        recommendKeyword.setMap(map);
    }

    public NavigableMap<Double, String> getMap(){
        return recommendKeyword.getMap();
    }

    class RandomCollection<E> {
        private NavigableMap<Double, E> map = new TreeMap<Double, E>();
        private final Random random;
        private double total = 0;
        private final int UPDATE_TIMES = 20;
        private int updateCnt = 0;

        public RandomCollection() {
            this(new Random());
        }

        public RandomCollection(Random random) {
            this.random = random;
        }

        public void setMap(NavigableMap<Double, E> map){
            this.map = map;
        }

        public NavigableMap<Double, E> getMap(){
            return map;
        }

        public RandomCollection<E> add(double weight, E result) {
            if (weight <= 0) return this;

            // 看要不要开起来，复杂度有点高O(nlogn)，但是感觉可以调节推荐比例
//            if(++updateCnt == UPDATE_TIMES){
//                updateCnt = 0;
//                NavigableMap<Double, E> newMap = new TreeMap<>();
//                for(Map.Entry entry : map.entrySet()){
//                    newMap.put((Double)entry.getKey() * 0.5, (E) entry.getValue());
//                }
//                total *= 0.5;
//                map = newMap;
//            }

            total += weight;
            map.put(total, result);
            return this;
        }

        public E next() {
            double value = random.nextDouble() * total;
            if(map.size() != 0)
                return map.higherEntry(value).getValue();
            return null;
        }

        public void reset(){
            map.clear();
        }
    }


}


