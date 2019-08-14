package com.example.newstoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mikepenz.materialdrawer.DrawerBuilder;

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

        new DrawerBuilder().withActivity(this).build();

        CatAdapter.OnItemClickListener listenerCat = new CatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String category) {
                news = newsManager.getNews(20, "2019-08-09", "2019-08-10", null, category);
                mAdapterNews.updateNews(news);
                mAdapterNews.notifyDataSetChanged();
            }
        };
        final NewsRepository newsRepository = new NewsRepository(AppDB.getAppDB(this));
        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news[position]);
                startActivity(intent);
//                newsRepository.insertNews(news[position]);
////                System.out.println("news has been inserted");
//                News[] newNews = newsRepository.getAllNews();
//                for(News news:newNews){
//                    System.out.println(news.getTitle());
//                }

            }
        };






        newsManager = new NewsManager();
        news = newsManager.getNews(20, "2019-08-09", "2019-08-10", null, "娱乐");

        recyclerViewNews = findViewById(R.id.table_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news);
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);

        recyclerViewCat = findViewById(R.id.cat_recycler_view);
        layoutManagerCat = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerCat).setOrientation(LinearLayout.HORIZONTAL);
        recyclerViewCat.setLayoutManager(layoutManagerCat);
        mAdapterCat = new CatAdapter();
        mAdapterCat.setOnItemClickListener(listenerCat);
        recyclerViewCat.setAdapter(mAdapterCat);


    }

}