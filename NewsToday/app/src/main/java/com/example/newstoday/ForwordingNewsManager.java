package com.example.newstoday;

import android.content.Context;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ForwordingNewsManager {
    /**
     * 所有用户的所有转发的消息都在一个数据库里存储
     * 此类的作用就是管理这个数据库
     * 此类的方法的方法名比较详实，通过方法名应该就可以得知对应方法的功能
     * 此类和UserMessageManager的功能大致相同。
     */
    private static ForwordingNewsManager INSTANCE = null;
    private AppDB forwordingNewsDB;
    private NewsDao forwordingNewsDao;
    private NewsRepository newsRepository;

    private ForwordingNewsManager(Context context){
        forwordingNewsDB = AppDB.getAppDB(context, "forwordingNews");
        forwordingNewsDao = forwordingNewsDB.newsDao();
        newsRepository = new NewsRepository(forwordingNewsDB);
    }

    public static ForwordingNewsManager getForwordingNewsManager(Context context){
        if(INSTANCE == null){
            INSTANCE = new ForwordingNewsManager(context);
        }
        return INSTANCE;
    }

    /**
     * 点转发就调用
     * @param news
     * @param email
     */
    public void addOneForwardingNewsForUser(News news, String email){
        news.setPublisher(email);
        news.setCategory("关注");
        String today = new SimpleDateFormat("yyyy-MM-dd\tHH:mm:ss", Locale.CHINA).format(new Date());
        news.setDate(today);
        newsRepository.insertNews(news);
    }

    /**
     * 得到一个人转发的所有消息
     * @param email
     * @return
     */
    public ArrayList<News> getForwardingNewsByEmail(String email){
        return newsRepository.getNewsByEmail(email);
    }

    /**此方法得到user所有关注的人的转发的消息*/
    public ArrayList<News> getUserAllFollowigNews(User user){
        String[] followigs = user.getFollowig().toArray(new String[user.getFollowig().size()]);
        return newsRepository.getNewsByEmail(followigs);
    }

    public ArrayList<News> getAllForwardingNews(){
        return newsRepository.getAllNews();
    }

    public void deleteOneForwardingNews(News... news){
        newsRepository.deleteNews(news);
    }

    public void deleteAllForwardingNewsOfUser(String email){
        newsRepository.deleteNewsByEmail(email);
    }


}
