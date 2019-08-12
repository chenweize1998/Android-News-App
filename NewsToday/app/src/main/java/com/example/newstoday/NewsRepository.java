package com.example.newstoday;
import android.os.AsyncTask;

public class NewsRepository {

    private final NewsDao newsDao;
    private final AppDB appDB;

    NewsRepository(AppDB appDB){
        this.appDB = appDB;
        this.newsDao = this.appDB.newsDao();
    }

    public void insertOneNews(News news){
        InsertOneNewsTask insertOneNewsTask = new InsertOneNewsTask();
        insertOneNewsTask.execute(news);

    }

    private class InsertOneNewsTask extends AsyncTask<News, Void, Void>{

        @Override
        protected Void doInBackground(News... news){
            newsDao.insert(news[0]);
            return null;
        }
    }



}
