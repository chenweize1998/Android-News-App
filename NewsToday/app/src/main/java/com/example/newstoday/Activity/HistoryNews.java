package com.example.newstoday.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.Adapter.CatAdapter;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.BaseDrawerItem;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.ArrayList;

public class HistoryNews extends AppCompatActivity {

    private RecyclerView recyclerViewNews;
    private NewsAdapter mAdapterNews;
    private RecyclerView.LayoutManager layoutManagerNews;
    private ArrayList<News> news;
    private NewsManager newsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_news);

        newsManager = NewsManager.getNewsManager(getApplicationContext());
        news = newsManager.getAllHistoryNews();
        for(News _news: news){
            _news.setImage(News.stringParse(_news.getOriImage()));
        }


        BaseDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("我的收藏")
                .withIcon(R.drawable.star).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("浏览历史")
                .withIcon(R.drawable.history).withTextColor(Color.parseColor("#ababab"));
        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem().withIdentifier(3).withName("夜间模式")
                .withIcon(R.drawable.night).withTextColor(Color.parseColor("#ababab"));
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName("Weize Chen")
                                .withEmail("wei10@mails.tsinghua.edu.cn").withIcon(R.drawable.chenweize)
                )
                .addProfiles(
                        new ProfileDrawerItem().withName("Hao Peng")
                                .withEmail("h-peng17@mails.tsinghua.edu.cn").withIcon(R.drawable.penghao)
                )
                .withTextColor(Color.parseColor("#ababab"))
                .build();
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withSelectedItem(2)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        switchDrawerItem
                )
                .withSliderBackgroundDrawableRes(R.drawable.drawer_bg)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem){
                        // do something with the clicked item :D
                        if(drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(getApplicationContext(), CollectionNews.class);
//                            startActivityForResult(intent, COLLECTION_CHANGED);
                            startActivity(intent);
                        }else if(drawerItem.getIdentifier() == 2) {
                            Intent intent = new Intent(getApplicationContext(), HistoryNews.class);
                            startActivity(intent);
//                            startActivityForResult(intent, HISTORY_CHANGED);

                        }

                        return false;
                    }
                })
                .build();


//        System.out.println(news.isEmpty());
        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);

            }
        };


        recyclerViewNews = findViewById(R.id.table_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news);
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);

    }

    @Override
    public void onBackPressed() {
//        Intent intent = getIntent();
//        setResult(Activity.RESULT_OK, intent);
        Intent intent = new Intent(getApplicationContext(), Table.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
//        finish();
    }

}
