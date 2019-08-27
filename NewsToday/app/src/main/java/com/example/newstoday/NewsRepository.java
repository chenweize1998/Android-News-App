package com.example.newstoday;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class NewsRepository {

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
    /****/

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
}
