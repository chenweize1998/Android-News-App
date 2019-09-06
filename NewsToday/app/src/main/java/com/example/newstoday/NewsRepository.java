package com.example.newstoday;
import android.os.AsyncTask;

import androidx.room.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class NewsRepository {

    /**
     * 此类不应该是单例模式
     * 因为我会用到此类的两个对象
     * 一个对象操作新闻界面的news
     * 一个对象操作用户转发的news
     * */

    private final NewsDao newsDao;
    private final AppDB appDB;

    NewsRepository(AppDB appDB){
        this.appDB = appDB;
        this.newsDao = this.appDB.newsDao();
    }

    /**
     * insert news to database
     * */
    public void insertNews(News... news){
        InsertNewsTask insertNewsTask = new InsertNewsTask();
        insertNewsTask.execute(news);
    }

    private class InsertNewsTask extends AsyncTask<News, Void, Void>{

        @Override
        protected Void doInBackground(News... news){
            newsDao.insert(news);
            return null;
        }
    }
    /********/

    /**
     *get all news from database
     */
    public ArrayList<News> getAllNews(){
        try {
            GetAllNewsTask getAllNewsTask = new GetAllNewsTask();
            return new ArrayList<News>(Arrays.asList(getAllNewsTask.execute(0).get()));
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllNewsTask extends AsyncTask<Integer, Void, News[]>{

        @Override
        protected  News[] doInBackground(Integer... params){
            return newsDao.getAllNews();
        }
    }


    /**
     * get news by email
     */
    public ArrayList<News> getNewsByEmail(String... email){
        try{
            GetNewsByEmailTask getNewsByEmailTask = new GetNewsByEmailTask();
            return new ArrayList(Arrays.asList(getNewsByEmailTask.execute(email).get()));
        }catch (ExecutionException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetNewsByEmailTask extends AsyncTask<String, Void, News[]>{
        @Override
        protected News[] doInBackground(String... email){
            return newsDao.getNewsByEmail(email);
        }
    }

    /**
     * delete news
     * */
    public void deleteNews(News... news){
        DeleteNewsTask deleteNewsTask = new DeleteNewsTask();
        deleteNewsTask.execute(news);

    }

    private class DeleteNewsTask extends AsyncTask<News, Void, Void>{

        @Override
        protected Void doInBackground(News... news){
            newsDao.delete(news);
            return null;
        }
    }
    /****/

    /**
     * delte news by email
     * */
    public void deleteNewsByEmail(String... email){
        DeleteNewsByEmailTask deleteNewsByEmailTask = new DeleteNewsByEmailTask();
        deleteNewsByEmailTask.execute(email);
    }

    private class DeleteNewsByEmailTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... email){
            newsDao.deleteNewsByEmail(email[0]);
            return null;
        }
    }

    /**
     * clear the table
     */
    public void clearNews(){
        ClearNewsTask clearNewsTask = new ClearNewsTask();
        clearNewsTask.execute(0);
    }

    private class ClearNewsTask extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... params){
            newsDao.clear();
            return null;
        }
    }


    /**
     * get all newsID
     */
    public String[] getAllNewsID(){
        try{
            GetAllNewsIDTask getAllNewsIDTask = new GetAllNewsIDTask();
            return getAllNewsIDTask.execute(0).get();
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    private class GetAllNewsIDTask extends AsyncTask<Integer, Void, String[]>{
        @Override
        protected String[] doInBackground(Integer... params){return newsDao.getAllNewsID();}
    }

    /**
     * Update news
     * */
    public void updateNews(News...news){
        UpdateNewsTask updateNewsTask = new UpdateNewsTask();
        updateNewsTask.execute(news);
    }

    private class UpdateNewsTask extends AsyncTask<News, Void, Void>{
        @Override
        protected Void doInBackground(News...news){
            newsDao.update(news);
            return null;
        }
    }
}
