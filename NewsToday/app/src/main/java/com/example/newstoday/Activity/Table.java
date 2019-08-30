package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArraySet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.FragmentTransitionSupport;

import com.example.newstoday.Adapter.CatAdapter;
import com.example.newstoday.News;
import com.example.newstoday.Adapter.NewsAdapter;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.example.newstoday.UserManagerOnServer;
import com.example.newstoday.AsyncServerNews;
import com.example.newstoday.WechatShareManager;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;
//import com.romainpiel.titanic.library.Titanic;
//import com.romainpiel.titanic.library.TitanicTextView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import dmax.dialog.SpotsDialog;

import static com.example.newstoday.Activity.NewsItem.mAdapterNews;

public class Table extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;

//    private RecyclerView recyclerViewNews;
//    private NewsAdapter mAdapterNews;
//    private CatAdapter mAdapterCat;
//
//    private ArrayList<News> news;
    private NewsManager newsManager;
//    private String currentCategory = "推荐";

    private AsyncServerNews asyncServerNews;
    private UserManagerOnServer userManagerOnServer;
//    private Titanic titanic;
    private DonutProgress donutProgress;
    private Timer timer;
    private AlertDialog spotsDialog;

    public static Drawer drawer;
    public AccountHeader header;
    private ArraySet<String> account = new ArraySet<>();
    private int identifier = 3;
    private int position = 0;

    private static final int DISMISS_TIMEOUT = 500;

    private final int CAT_REARRANGE = 1;
    private final int LOGIN_REQUEST = 2;
    private final int PICK_IMAGE = 3;
    private final int LOGOUT_REQUEST = 4;

    private final int COLLECTION_IDENTIFIER = 1;
    private final int HISTORY_IDENTIFIER = 2;
    private final int CLEAR_IDENTIFIER = 3;
    private final int NIGHT_IDENTIFIER = 4;
    private final int UPLOAD_IDENTIFIER = 5;
    private final int DOWNLOAD_IDENTIFIER = 6;

    private final int LOGIN_IDENTIFIER = 1;
    private final int LOGOUT_IDENTIFIER = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        NewsItem newsItem = new NewsItem();
        fragmentTransaction.add(R.id.table_fragment, newsItem);
        fragmentTransaction.commit();

        newsManager = NewsManager.getNewsManager(getApplicationContext());

        asyncServerNews = AsyncServerNews.getAsyncServerNews(getApplicationContext());
        userManagerOnServer = UserManagerOnServer.getUserManagerOnServer();

        /**
         * Drawer
         */
        buildDrawer();

//        /**
//         * Category arrangement button
//         */
//        ImageButton imgButton = findViewById(R.id.cat_arange);
//        imgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), CategoryArrangement.class);
//                intent.putExtra("cat", mAdapterCat.category);
//                intent.putExtra("delCat", mAdapterCat.delCategory);
//                startActivityForResult(intent, CAT_REARRANGE);
//            }
//        });

//        /**
//         * Category click event
//         */
//        CatAdapter.OnItemClickListener listenerCat = new CatAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, String category) {
//                Table.this.currentCategory = category;
//                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
//                if(newsManager.getLastCategory() != category){
//                    news = newsManager.getNews(20, "2019-08-09", today, null, currentCategory, false, false);
//                    mAdapterNews.updateNews(news);
//                    mAdapterNews.notifyDataSetChanged();
//                    recyclerViewNews.smoothScrollToPosition(0);
//                }
//                else
//                    recyclerViewNews.smoothScrollToPosition(0);
//            }
//        };

        /**
         * Wechat share
         */
        final WechatShareManager wsm = WechatShareManager.getInstance(this);

//        /**
//         * NewsListener
//         */
//        NewsAdapter.OnItemClickListener listenerNews = new NewsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, final View v) {
//                Intent intent = new Intent(getApplicationContext(), NewsPage.class);
//                intent.putExtra("news", news.get(position));
//                startActivity(intent);
//            }
//        };

        /**
         * News items
         */
//        RefreshLayout refreshLayout = findViewById(R.id.item_refresh_layout);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(final RefreshLayout refreshlayout) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Table.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
//                                ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
//                                        today, null, currentCategory, true, true);
//                                mAdapterNews.updateNews(newsTmp);
//                                refreshlayout.finishRefresh();
//                                mAdapterNews.notifyDataSetChanged();
//                                Toast.makeText(getApplicationContext(), "刷新完成", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                }, DISMISS_TIMEOUT);
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(final RefreshLayout refreshlayout) {
//                new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Table.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
//                                    ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
//                                            today, null, currentCategory, true, false);
//                                    mAdapterNews.refreshNews(newsTmp);
//                                    refreshlayout.finishLoadMore();
//                                    mAdapterNews.notifyDataSetChanged();
//                                    Toast.makeText(getApplicationContext(), "新返回"+newsTmp.size()+"条新闻", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    }, DISMISS_TIMEOUT);
//            }
//        });
//
//
//        /**
//         * News and Category recycler view
//         */
//        newsManager = NewsManager.getNewsManager(this);
//        newsManager.resetPageCounter();
//        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
//        ArrayList<News> newsTmp = newsManager.getNews(20, "2019-08-09",
//                today, null, currentCategory, true, true);
//        news = new ArrayList<>();
//        news.addAll(newsTmp);
//
//        recyclerViewNews = findViewById(R.id.table_recycler_view);
//        RecyclerView.LayoutManager layoutManagerNews = new LinearLayoutManager(this);
//        recyclerViewNews.setLayoutManager(layoutManagerNews);
//        mAdapterNews = new NewsAdapter(news, Table.this);
//        mAdapterNews.setOnItemClickListener(listenerNews);
//        recyclerViewNews.setAdapter(mAdapterNews);
//        recyclerViewNews.setItemViewCacheSize(100);
//
//        RecyclerView recyclerViewCat = findViewById(R.id.cat_recycler_view);
//        recyclerViewCat.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManagerCat = new LinearLayoutManager(this);
//        ((LinearLayoutManager) layoutManagerCat).setOrientation(RecyclerView.HORIZONTAL);
//        recyclerViewCat.setLayoutManager(layoutManagerCat);
//        mAdapterCat = new CatAdapter();
//        mAdapterCat.setOnItemClickListener(listenerCat);
//        recyclerViewCat.setAdapter(mAdapterCat);
//        recyclerViewCat.setItemViewCacheSize(5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == CAT_REARRANGE) {
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
        } else */if(requestCode == LOGIN_REQUEST){
            if(resultCode == RESULT_OK){
                String email = (String) data.getSerializableExtra("email");
                if(account.contains(email)){
                    Toast.makeText(getApplicationContext(), "账号已登陆", Toast.LENGTH_LONG).show();
                    return;
                }
                account.add(email);
                String name = (String) data.getSerializableExtra("name");
                header.addProfile(new ProfileDrawerItem().withName(name)
                        .withEmail(email).withIdentifier(identifier)
                        .withIcon(R.drawable.header), position);
                header.setActiveProfile(identifier, true);
                header.updateProfile(header.getProfiles().get(identifier - 1));
                ++identifier;
                ++position;
            }
        } else if(requestCode == PICK_IMAGE){
            try {
                if(data == null)
                    return;
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                header.getActiveProfile().withIcon(bitmap);
                header.updateProfile(header.getActiveProfile());
            } catch (FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "更换头像失败", Toast.LENGTH_SHORT);
            }
        }
    }

    private void buildDrawer(){
        BaseDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(COLLECTION_IDENTIFIER).withName("我的收藏")
                .withIcon(R.drawable.ic_star).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(HISTORY_IDENTIFIER).withName("浏览历史")
                .withIcon(R.drawable.ic_history).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(UPLOAD_IDENTIFIER).withName("上传用户数据")
                .withIcon(R.drawable.ic_cloud_upload).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(DOWNLOAD_IDENTIFIER).withName("下载用户数据")
                .withIcon(R.drawable.ic_cloud_download).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item5 = new SecondaryDrawerItem().withIdentifier(CLEAR_IDENTIFIER).withName("清除历史")
                .withIcon(R.drawable.clear).withTextColor(Color.parseColor("#ababab"));
        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem().withIdentifier(NIGHT_IDENTIFIER).withName("夜间模式")
                .withIcon(R.drawable.night).withTextColor(Color.parseColor("#ababab")).withSelectable(false);
        header = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
//                        new ProfileDrawerItem().withName("Weize Chen").withIdentifier(3)
//                        .withEmail("chenweize@mails.tsinghua.edu.cn").withIcon(R.drawable.chenweize),
//                        new ProfileDrawerItem().withName("Hao Peng").withIdentifier(4)
//                                .withEmail("h-peng17@mails.tsinghua.edu.cn").withIcon(R.drawable.penghao)
                )
                .addProfiles(
                        new ProfileSettingDrawerItem().withName("Add Account")
                                .withIcon(R.drawable.plus).withIdentifier(LOGIN_IDENTIFIER),
                        new ProfileSettingDrawerItem().withName("Log Out")
                                .withIcon(R.drawable.ic_exit_to_app).withIdentifier(LOGOUT_IDENTIFIER))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile.getIdentifier() == LOGIN_IDENTIFIER){
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivityForResult(intent, LOGIN_REQUEST);
                        } else if (profile.getIdentifier() == LOGOUT_IDENTIFIER){
                            if(identifier == 3) {
                                Toast.makeText(getApplicationContext(), "当前没有用户登录", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            account.remove(header.getActiveProfile().getEmail().toString());
                            header.removeProfileByIdentifier(header.getActiveProfile().getIdentifier());
                            --identifier;
                            --position;
                            if(identifier != 3)
                                header.setActiveProfile(identifier - 1);
                            newsManager.deleteAllHistory();
                            newsManager.deleteAllCollection();
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
                        } else {
                            header.setActiveProfile(profile.getIdentifier(), true);
                            newsManager.deleteAllCollection();
                            newsManager.deleteAllHistory();
                        }
                        return true;
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
                        item4,
                        item5,
                        new DividerDrawerItem(),
                        switchDrawerItem
                )
                .withSliderBackgroundDrawableRes(R.drawable.drawer_bg)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem){
                        if(drawerItem.getIdentifier() == COLLECTION_IDENTIFIER) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            CollectionNews collectionNews = new CollectionNews();
                            fragmentTransaction.replace(R.id.table_fragment, collectionNews);
                            if(fragmentManager.getBackStackEntryCount() == 0)
                                fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        } else if (drawerItem.getIdentifier() == HISTORY_IDENTIFIER) {
//                            Intent intent = new Intent(getApplicationContext(), HistoryNews.class);
//                            startActivity(intent);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            HistoryNews historyNews = new HistoryNews();
                            fragmentTransaction.replace(R.id.table_fragment, historyNews);
                            if(fragmentManager.getBackStackEntryCount() == 0)
                                fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        } else if (drawerItem.getIdentifier() == CLEAR_IDENTIFIER) {
                            newsManager.deleteAllHistory();
                            mAdapterNews.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "历史记录已清除", Toast.LENGTH_LONG).show();
                        } else if (drawerItem.getIdentifier() == UPLOAD_IDENTIFIER) {
                            asyncServerNews.asyncCollectionNewsToServer();
                            asyncServerNews.asyncHistoryNewsToServer();
                        } else if (drawerItem.getIdentifier() == DOWNLOAD_IDENTIFIER) {
                            newsManager.deleteAllHistory();
                            asyncServerNews.asyncHistoryNewsFromServer();
                            newsManager.deleteAllCollection();
                            asyncServerNews.asyncCollectionNewsFromServer();
                            mAdapterNews.notifyDataSetChanged();
//                            TitanicTextView myTitanicTextView = findViewById(R.id.titanic_tv);
//                            titanic = new Titanic();
////                            myTitanicTextView.setVisibility(View.VISIBLE);
//                            titanic.start(myTitanicTextView);


//                            donutProgress = findViewById(R.id.donut_progress);
//                            donutProgress.setVisibility(View.VISIBLE);
//                            donutProgress.setUnfinishedStrokeColor(R.color.unfinishedBar);
//                            donutProgress.setFinishedStrokeColor(R.color.finishedBar);
//                            timer = new Timer();
//                            timer.schedule(new TimerTask() {
//
//                                @Override
//                                public void run() {
//                                    if (donutProgress.getProgress()>=100){
//                                        cancel();
//                                        timer.cancel();
//                                        donutProgress.setVisibility(View.INVISIBLE);
//                                    }
//                                    runOnUiThread(new Runnable() {
//
//                                        @Override
//                                        public void run() {
//                                            donutProgress.setProgress(donutProgress.getProgress() + 1);
//
//                                        }
//                                    });
//                                }
//                            }, 300, 20);
////                            timer.cancel();
                        spotsDialog = new SpotsDialog.Builder()
                                .setContext(Table.this)
                                .setCancelable(false)
                                .setTheme(R.style.Custom)
                                .build();
                        spotsDialog.show();


                        //这个线程没有显式的关闭，不知道会不会有点问题
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                }catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                spotsDialog.dismiss();
                            }
                        }).start();



                        }
                        return false;
                    }
                })
                .build();
    }

    @Override
    protected void onStop (){
        super.onStop();
        newsManager.resetRecommendation();
    }

    @Override
    protected void onNewIntent(Intent intent){
//        mAdapterNews.notifyDataSetChanged();
//        drawer.setSelectionAtPosition(-1);
    }

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
        else {
            newsManager.resetRecommendation();
            getSupportFragmentManager().popBackStack();
            drawer.setSelectionAtPosition(-1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        newsManager.deleteAllCollection();
        newsManager.deleteAllHistory();
    }
}