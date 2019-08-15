package com.example.newstoday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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

        BaseDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("我的收藏")
                .withIcon(R.mipmap.star).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("浏览历史")
                .withIcon(R.mipmap.history).withTextColor(Color.parseColor("#ababab"));
        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem().withIdentifier(3).withName("夜间模式")
                .withIcon(R.mipmap.night).withTextColor(Color.parseColor("#ababab"));
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName("Weize Chen")
                        .withEmail("wei10@mails.tsinghua.edu.cn").withIcon(R.mipmap.chenweize)
                )
                .addProfiles(
                        new ProfileDrawerItem().withName("Hao Peng")
                                .withEmail("h-peng17@mails.tsinghua.edu.cn").withIcon(R.mipmap.penghao)
                )
                .withTextColor(Color.parseColor("#ababab"))
                .build();
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        switchDrawerItem
                )
                .withSliderBackgroundDrawableRes(R.mipmap.drawer_bg)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem){
                        // do something with the clicked item :D
                        return false;
                    }
                })
                .build();

        CatAdapter.OnItemClickListener listenerCat = new CatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String category) {
                news = newsManager.getNews(20, "2019-08-09", "2019-08-10", null, category);
                mAdapterNews.updateNews(news);
                mAdapterNews.notifyDataSetChanged();
            }
        };

        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news[position]);
                startActivity(intent);
//                historyNews.insertNews(news[position]);
//                System.out.println("news has been inserted");
//                News[] newNews = historyNews.getAllNews();
//                for(News news:newNews){
//                    System.out.println(news.getTitle());
//                }

            }
        };






        newsManager = NewsManager.getNewsManager(this);
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