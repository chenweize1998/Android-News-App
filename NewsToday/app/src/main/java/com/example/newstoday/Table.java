package com.example.newstoday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

public class Table extends AppCompatActivity {
    private RecyclerView recyclerViewNews;
    private NewsAdapter mAdapterNews;
    private RecyclerView.LayoutManager layoutManagerNews;
    private RecyclerView recyclerViewCat;
    //    private RecyclerView.Adapter mAdapterCat;
    private CatAdapter mAdapterCat;
    private RecyclerView.LayoutManager layoutManagerCat;
    private String[] title, abs;
    private News[] news;
    private NewsManager newsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);

        CatAdapter.OnItemClickListener listener = new CatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String category) {
                news = newsManager.getNews(20, "2019-08-09", "2019-08-10", null, category);
                mAdapterNews.updateNews(news);
                mAdapterNews.notifyDataSetChanged();
            }
        };


        NewsRepository newsRepository = new NewsRepository(AppDB.getAppDB(this));
//        newsRepository.insertOneNews(news[0]);



        newsManager = new NewsManager();
        news = newsManager.getNews(20, "2019-08-09", "2019-08-10", null, "娱乐");

        recyclerViewNews = findViewById(R.id.table_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news);
        recyclerViewNews.setAdapter(mAdapterNews);

        recyclerViewCat = findViewById(R.id.cat_recycler_view);
        layoutManagerCat = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerCat).setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewCat.setLayoutManager(layoutManagerCat);
        mAdapterCat = new CatAdapter();
        mAdapterCat.setOnItemClickListener(listener);
        recyclerViewCat.setAdapter(mAdapterCat);


    }

}