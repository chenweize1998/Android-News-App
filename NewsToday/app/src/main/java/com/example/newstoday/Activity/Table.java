package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.newstoday.Adapter.CatAdapter;
import com.example.newstoday.News;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.example.newstoday.UserManagerOnServer;
import com.example.newstoday.AsyncServerNews;
import com.example.newstoday.WechatShareManager;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class Table extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;

    private RecyclerView recyclerViewNews;
    private NewsAdapter mAdapterNews;
    private CatAdapter mAdapterCat;

    private ArrayList<News> news;
    private NewsManager newsManager;
    private String currentCategory = "推荐";

    private AsyncServerNews asyncServerNews;
    private UserManagerOnServer userManagerOnServer;

    private Drawer drawer;
    private AccountHeader header;

    private static final int DISMISS_TIMEOUT = 500;

    private final int CAT_REARRANGE = 1;
    private final int USER_REQUEST = 2;
    private final int PICK_IMAGE = 3;

    private final int COLLECTION_IDENTIFIER = 1;
    private final int HISTORY_IDENTIFIER = 2;
    private final int CLEAR_IDENTIFIER = 3;
    private final int NIGHT_IDENTIFIER = 4;
    private final int LOGIN_IDENTIFIER = 5;
    private final int LOGOUT_IDENTIFIER = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);

        asyncServerNews = AsyncServerNews.getAsyncServerNews(getApplicationContext());
        userManagerOnServer = UserManagerOnServer.getUserManagerOnServer();

        /**
         * Drawer
         */
        BaseDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(COLLECTION_IDENTIFIER).withName("我的收藏")
                .withIcon(R.drawable.star).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(HISTORY_IDENTIFIER).withName("浏览历史")
                .withIcon(R.drawable.history).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(CLEAR_IDENTIFIER).withName("清除历史")
                .withIcon(R.drawable.clear).withTextColor(Color.parseColor("#ababab"));
        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem().withIdentifier(NIGHT_IDENTIFIER).withName("夜间模式")
                .withIcon(R.drawable.night).withTextColor(Color.parseColor("#ababab")).withSelectable(false);
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName("Weize Chen").withIdentifier(1)
                        .withEmail("wei10@mails.tsinghua.edu.cn").withIcon(R.drawable.chenweize),
                        new ProfileDrawerItem().withName("Hao Peng").withIdentifier(2)
                                .withEmail("h-peng17@mails.tsinghua.edu.cn").withIcon(R.drawable.penghao)
                )
                .addProfiles(
                        new ProfileSettingDrawerItem().withName("Add Account")
                                .withIcon(R.drawable.plus).withIdentifier(LOGIN_IDENTIFIER),
                        new ProfileSettingDrawerItem().withName("Manage Your Account")
                                .withIcon(R.drawable.setting).withIdentifier(LOGOUT_IDENTIFIER))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile.getIdentifier() == LOGIN_IDENTIFIER){
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivityForResult(intent, USER_REQUEST);
                        }
                        return false;
                    }
                })
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        if(header.getActiveProfile() == profile) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                        }
                        header.setActiveProfile(profile.getIdentifier(), true);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withTextColor(Color.parseColor("#ababab"))
                .build();
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withSelectedItem(-1)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        switchDrawerItem
                )
                .withSliderBackgroundDrawableRes(R.drawable.drawer_bg)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem){
                        // do something with the clicked item :D
                        if(drawerItem.getIdentifier() == COLLECTION_IDENTIFIER) {
                            Intent intent = new Intent(getApplicationContext(), CollectionNews.class);
                            startActivity(intent);

//                            startActivityForResult(intent, COLLECTION_CHANGED);
//                            if(userManagerOnServer.userSignUp("h-peng17", "123456")){
//                                System.out.println("用户注册成功");
//                            }else{
//                                System.out.println("用户注册失败");
//                            }
                        } else if (drawerItem.getIdentifier() == HISTORY_IDENTIFIER) {
                            Intent intent = new Intent(getApplicationContext(), HistoryNews.class);
                            startActivity(intent);
//                            startActivityForResult(intent, HISTORY_CHANGED);
//                            if(userManagerOnServer.userSignOut()){
//                                System.out.println("用户退出成功");
//                            }else{
//                                System.out.println("用户退出失败");
////                            }
//                            if(userManagerOnServer.userSignIn("h-peng17","123456")){
//                                System.out.println("用户登录成功");
//                            }else{
//                                System.out.println("用户登陆失败");
//                            }
//                            if(asyncServerNews.asyncHistoryNewsFromServer()){
//                                System.out.println("同步成功");
//                            }else{
//                                System.out.println("同步失败");
//                            }
                        } else if (drawerItem.getIdentifier() == CLEAR_IDENTIFIER) {
                            newsManager.deleteAllHistory();
                            mAdapterNews.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "历史记录已清除", Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                })
                .build();

        /**
         * Search view
         */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = findViewById(R.id.table_searchView);
        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

        /**
         * Category arrangement button
         */
        ImageButton imgButton = findViewById(R.id.cat_arange);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CategoryArrangement.class);
                intent.putExtra("cat", mAdapterCat.category);
                intent.putExtra("delCat", mAdapterCat.delCategory);
                startActivityForResult(intent, CAT_REARRANGE);
            }
        });

        /**
         * Category click event
         */
        CatAdapter.OnItemClickListener listenerCat = new CatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String category) {
                Table.this.currentCategory = category;
                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                if(newsManager.getLastCategory() != category){
                    news = newsManager.getNews(20, "2019-08-09", today, null, currentCategory, false, false);
                    mAdapterNews.updateNews(news);
                    mAdapterNews.notifyDataSetChanged();
                    recyclerViewNews.smoothScrollToPosition(0);
                }
                else
                    recyclerViewNews.smoothScrollToPosition(0);
            }
        };

        /**
         * Wechat share
         */
        final WechatShareManager wsm = WechatShareManager.getInstance(this);
        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, final View v) {
                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
                intent.putExtra("news", news.get(position));
                startActivity(intent);
            }
        };

        /**
         * News items
         */
//        mSwipyRefreshLayout = findViewById(R.id.item_swipyrefresh);
//        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//                if(direction == SwipyRefreshLayoutDirection.TOP) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Table.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mSwipyRefreshLayout.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }, DISMISS_TIMEOUT);
//                }
//                else if(direction == SwipyRefreshLayoutDirection.BOTTOM){
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Table.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
//                                    ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
//                                            today, null, currentCategory, true, false);
//                                    mAdapterNews.refreshNews(newsTmp);
//                                    mAdapterNews.notifyDataSetChanged();
//                                    mSwipyRefreshLayout.setRefreshing(false);
//                                    recyclerViewNews.smoothScrollToPosition(mAdapterNews.getItemCount() - newsTmp.size());
//                                    Toast.makeText(getApplicationContext(), "新返回"+newsTmp.size()+"条新闻", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                    }, DISMISS_TIMEOUT);
//                }
//            }
//        });
        RefreshLayout refreshLayout = findViewById(R.id.item_refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshlayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Table.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                                ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                                        today, null, currentCategory, true, true);
                                mAdapterNews.updateNews(newsTmp);
                                refreshlayout.finishRefresh();
                                mAdapterNews.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(), "刷新完成", Toast.LENGTH_SHORT).show();
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
                            Table.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                                    ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                                            today, null, currentCategory, true, false);
                                    mAdapterNews.refreshNews(newsTmp);
                                    refreshlayout.finishLoadMore();
                                    mAdapterNews.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "新返回"+newsTmp.size()+"条新闻", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, DISMISS_TIMEOUT);
            }
        });


        /**
         * News and Category recycler view
         */
        newsManager = NewsManager.getNewsManager(this);
        newsManager.resetPageCounter();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
        ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
                today, null, currentCategory, true, true);
        news = new ArrayList<>();
        news.addAll(newsTmp);

        recyclerViewNews = findViewById(R.id.table_recycler_view);
        RecyclerView.LayoutManager layoutManagerNews = new LinearLayoutManager(this);
        recyclerViewNews.setLayoutManager(layoutManagerNews);
        mAdapterNews = new NewsAdapter(news, Table.this);
        mAdapterNews.setOnItemClickListener(listenerNews);
        recyclerViewNews.setAdapter(mAdapterNews);
        recyclerViewNews.setItemViewCacheSize(100);

        RecyclerView recyclerViewCat = findViewById(R.id.cat_recycler_view);
        recyclerViewCat.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerCat = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManagerCat).setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCat.setLayoutManager(layoutManagerCat);
        mAdapterCat = new CatAdapter();
        mAdapterCat.setOnItemClickListener(listenerCat);
        recyclerViewCat.setAdapter(mAdapterCat);
        recyclerViewCat.setItemViewCacheSize(5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        } else if(requestCode == USER_REQUEST){
            if(resultCode == RESULT_OK){
                String email = (String) data.getSerializableExtra("email");
                String name = (String) data.getSerializableExtra("name");
                header.addProfiles(new ProfileDrawerItem().withName(name)
                        .withEmail(email).withIdentifier(header.getProfiles().size()));
                header.setActiveProfile(header.getProfiles().get(header.getProfiles().size() - 1), true);
            }
        } else if(requestCode == PICK_IMAGE){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                header.getActiveProfile().withIcon(bitmap);
                header.updateProfile(header.getActiveProfile());
            } catch (FileNotFoundException e){
                e.printStackTrace();
                System.exit(-1);                // 调试完以后注释掉！！！！！！！！！！！！！！！！！！！！
            }

        }
    }

    @Override
    protected void onStop (){
        super.onStop();
        newsManager.resetRecommendation();
    }

    @Override
    protected void onNewIntent(Intent intent){
        mAdapterNews.notifyDataSetChanged();
        drawer.setSelectionAtPosition(-1);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}