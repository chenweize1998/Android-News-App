package com.example.newstoday;

import android.content.Context;
import android.os.AsyncTask;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class UserMessageManager {

    /**
     * 所有用户的所有发布的消息都存在一个共同的数据库里面。
     * 此类的作用就是控制管理这个数据库。
     * 此类的方法名比较详实，应该可以看懂对应方法的功能。
     * 此类的功能和ForwordingNewsManager类大致相同。
     *
     * 重新写了这个类，userMessage是News类型的，你只需要
     * 在创建的时候记得把image的url改成本地的uri/改成某个网址(之后再说)
     * 把newsID改为自己的ID
     * publisher改成自己
     * 总之就是这个新闻是自己的
     * */

    private static UserMessageManager INSTANCE;
    private AppDB userMessageDB;
    private NewsRepository newsRepository;

    private UserMessageManager(Context context){
        userMessageDB = AppDB.getAppDB(context, "userMessage");
        newsRepository = new NewsRepository(userMessageDB);
    }

    public static UserMessageManager getUserMessageManager(Context context){
        if(INSTANCE == null){
            INSTANCE = new UserMessageManager(context);
        }
        return INSTANCE;
    }

    /**
     * 发布一条message加进数据库，发布就调用
     * @param news
     */
    public void addOneUserMessage(News... news){
        newsRepository.insertNews(news);
    }

    public ArrayList<News> getAllUserMessage(){
        return newsRepository.getAllNews();
    }

    /**
     * 得到某个人的所有message
     * @param email
     * @return
     */
    public ArrayList<News> getCerternUserMessageByUserEmail(String... email){
        return newsRepository.getNewsByEmail(email);
    }

    /**
     * 所有关注人的message
     * @param user
     * @return
     */
    public ArrayList<News> getUserAllFollowigMessage(User user){
        /**这个地方需要修改*/
        String[] followigs = user.getFollowig().toArray(new String[user.getFollowig().size()]);
        return newsRepository.getNewsByEmail(followigs);
    }

    /**
     * 删除某条
     */
    public void deleteMessageByMessageID(News... news){
        newsRepository.deleteNews();
    }

    /**
     * 删除某个人的所有message
     * @param email
     */
    public void deleteMessageByUserEmail(String... email){
        newsRepository.deleteNewsByEmail(email);
    }

}
