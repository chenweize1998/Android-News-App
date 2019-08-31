package com.example.newstoday.Activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.Adapter.CatAdapter;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class NewsItem extends Fragment {
    private RecyclerView recyclerViewNews;
    public static NewsAdapter mAdapterNews;
    private CatAdapter mAdapterCat;
    private FragmentManager fragmentManager;

    private ArrayList<News> news;
    private NewsManager newsManager;
    private String currentCategory = "推荐";


    private int DISMISS_TIMEOUT = 500;

    private final int CAT_REARRANGE = 1;

    NewsItem(){

    }

    NewsItem(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        /**
         * Category click event
         */
        CatAdapter.OnItemClickListener listenerCat = new CatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String category) {
                currentCategory = category;
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                if (!newsManager.getLastCategory().equals(category)) {
                    news = newsManager.getNews(20, "2019-08-09", today, null, currentCategory, false, false);
                    mAdapterNews.updateNews(news);
                    mAdapterNews.notifyDataSetChanged();
                    recyclerViewNews.smoothScrollToPosition(0);
                } else
                    recyclerViewNews.smoothScrollToPosition(0);
            }
        };

        /**
         * News Listener
         */
        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getActivity(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);
            }
        };

        /**
         * Search view
         */
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = view.findViewById(R.id.table_searchView);
        ComponentName cn = new ComponentName(getActivity(), SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        /**
         * Category arrangement button
         */
        ImageButton imgButton = view.findViewById(R.id.cat_arange);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryArrangement.class);
                intent.putExtra("cat", mAdapterCat.category);
                intent.putExtra("delCat", mAdapterCat.delCategory);
                startActivityForResult(intent, CAT_REARRANGE);
            }
        });

        RefreshLayout refreshLayout = view.findViewById(R.id.item_refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                                ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                                        today, null, currentCategory, true, true);
                                mAdapterNews.updateNews(newsTmp);
                                refreshlayout.finishRefresh();
                                mAdapterNews.notifyDataSetChanged();
                                Toast.makeText(getActivity().getApplicationContext(), "刷新完成", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, DISMISS_TIMEOUT);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                                ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                                        today, null, currentCategory, true, false);
                                mAdapterNews.refreshNews(newsTmp);
                                refreshlayout.finishLoadMore();
                                mAdapterNews.notifyDataSetChanged();
                                Toast.makeText(getActivity().getApplicationContext(), "新返回" + newsTmp.size() + "条新闻", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, DISMISS_TIMEOUT);
            }
        });


        /**
         * News and Category recycler view
         */
        newsManager = NewsManager.getNewsManager(getActivity());
        newsManager.resetPageCounter();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
        ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                today, null, currentCategory, true, true);
        news = new ArrayList<>();
        news.addAll(newsTmp);

        recyclerViewNews = view.findViewById(R.id.table_recycler_view);
        RecyclerView.LayoutManager layoutManagerNews = new LinearLayoutManager(getContext());
        layoutManagerNews.setItemPrefetchEnabled(true);
        ((LinearLayoutManager) layoutManagerNews).setInitialPrefetchItemCount(20);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news, getActivity(), this.fragmentManager);
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);
        recyclerViewNews.setItemViewCacheSize(100);

        RecyclerView recyclerViewCat = view.findViewById(R.id.cat_recycler_view);
        recyclerViewCat.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerCat = new LinearLayoutManager(getContext());
        ((LinearLayoutManager) layoutManagerCat).setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCat.setLayoutManager(layoutManagerCat);
        mAdapterCat = new CatAdapter();
        mAdapterCat.setOnItemClickListener(listenerCat);
        recyclerViewCat.setAdapter(mAdapterCat);
        recyclerViewCat.setItemViewCacheSize(5);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAT_REARRANGE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> category = (ArrayList<String>) (data).getSerializableExtra("cat");
                ArrayList<String> delCategory = (ArrayList<String>) (data).getSerializableExtra("delCat");
                currentCategory = category.get(0);
                mAdapterCat.category = category;
                mAdapterCat.delCategory = delCategory;
                mAdapterCat.updateSelection();
                mAdapterCat.notifyDataSetChanged();
                newsManager.resetPageCounter();
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                        today, null, currentCategory, true, true);
                news.clear();
                news.addAll(newsTmp);
                mAdapterNews.updateNews(news);
                mAdapterNews.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
