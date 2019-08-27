package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerViewNews;
    private NewsAdapter mAdapterNews;
    private RecyclerView.LayoutManager layoutManagerNews;
    private ArrayList<News> news;
    private NewsManager newsManager;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    private final String category = "娱乐,军事,教育,文化,健康,财经,体育,汽车,科技,社会";
    private final int DISMISS_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_news);

        newsManager = NewsManager.getNewsManager(getApplicationContext());

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            news = newsManager.getNews(20, null, null, query, category, true, true);
        }

        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);
            }
        };

        mSwipyRefreshLayout = findViewById(R.id.item_swipyrefresh);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if(direction == SwipyRefreshLayoutDirection.TOP) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipyRefreshLayout.setRefreshing(false);
                                }
                            });
                        }
                    }, DISMISS_TIMEOUT);
                }
                else if(direction == SwipyRefreshLayoutDirection.BOTTOM){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SearchActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                                            "2019-08-10", null, category, true, false);
                                    mAdapterNews.refreshNews(newsTmp);
                                    mAdapterNews.notifyDataSetChanged();
                                    mSwipyRefreshLayout.setRefreshing(false);
                                    if(newsTmp != null)
                                        recyclerViewNews.smoothScrollToPosition(mAdapterNews.getItemCount() - newsTmp.size());
                                }
                            });
                        }
                    }, DISMISS_TIMEOUT);
                }
            }
        });

        recyclerViewNews = findViewById(R.id.table_recycler_view);
        layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news);
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);
    }
}
