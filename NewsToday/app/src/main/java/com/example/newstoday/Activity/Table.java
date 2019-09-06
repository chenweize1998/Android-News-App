package com.example.newstoday.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.newstoday.ImageFilePath;
import com.example.newstoday.ImagePoster;
import com.example.newstoday.NewsManager;
import com.example.newstoday.OfflineNewsManager;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
import com.example.newstoday.UserManagerOnServer;
import com.example.newstoday.AsyncServerNews;
import com.example.newstoday.UserMessageManager;
import com.example.newstoday.WechatShareManager;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.*;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;
import dmax.dialog.SpotsDialog;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.InputStreamProvider;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.example.newstoday.Activity.NewsItem.mAdapterNews;

public class Table extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;

    private NewsManager newsManager;

    private AsyncServerNews asyncServerNews;
    private AlertDialog spotsDialog;
    private UserManagerOnServer userManagerOnServer;
    private UserMessageManager userMessageManager;
    private UserManager userManager;
    private OfflineNewsManager offlineNewsManager;
    private ImagePoster imagePoster;
    private PersonalHomepage homepage;

    public Drawer drawer;
    public static AccountHeader header;
    private ArraySet<String> account = new ArraySet<>();
    private int identifier = 3;
    private int position = 0;

    private final int CAT_REARRANGE = 1;
    public static final int LOGIN_REQUEST = 2;
    public static final int PICK_IMAGE = 3;
    public static final int PUBLISH = 4;
    public static final int PUBLISH_CHOOSE_IMAGE = 5;

    public static final int COLLECTION_IDENTIFIER = 1;
    public static final int HISTORY_IDENTIFIER = 2;
    public static final int CLEAR_IDENTIFIER = 3;
    public static final int NIGHT_IDENTIFIER = 4;
    public static final int UPLOAD_IDENTIFIER = 5;
    public static final int DOWNLOAD_IDENTIFIER = 6;
    public static final int FILTER_IDENTIFIER = 7;
    public static final int SEARCH_IDENTIFIER = 8;

    public static final int LOGIN_IDENTIFIER = 1;
    public static final int LOGOUT_IDENTIFIER = 2;


    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_table);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
        NewsItem newsItem = new NewsItem(this.getSupportFragmentManager());
        fragmentTransaction.add(R.id.table_fragment, newsItem);
//        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

        newsManager = NewsManager.getNewsManager(getApplicationContext());
        userManager = UserManager.getUserManager(getApplicationContext());
        userManagerOnServer = UserManagerOnServer.getUserManagerOnServer(getApplicationContext());
        offlineNewsManager = OfflineNewsManager.getOfflineNewsManager(getApplicationContext());
        imagePoster = ImagePoster.getImagePoster(getApplicationContext());
        userMessageManager = UserMessageManager.getUserMessageManager(getApplicationContext());

        asyncServerNews = AsyncServerNews.getAsyncServerNews(getApplicationContext());
//        userManagerOnServer = UserManagerOnServer.getUserManagerOnServer();

        /**
         * Drawer
         */
        buildDrawer(this);

//        if(savedInstanceState != null) {
        Intent intent = getIntent();
        String[] email = intent.getStringArrayExtra("email");
        String[] name = intent.getStringArrayExtra("name");
        if (email != null) {
            for (int i = 0; i < email.length; ++i) {
                header.addProfile(new ProfileDrawerItem().withName(name[i]).withIdentifier(3 + i)
                        .withEmail(email[i]).withIcon(R.drawable.header), i);
            }
            if (header.getProfiles().size() > 2)
                header.setActiveProfile(intent.getLongExtra("Active ID", 1));
        }
//        }

        /**
         * Wechat share
         */
        final WechatShareManager wsm = WechatShareManager.getInstance(this);
//        userMessageManager.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST){
            if(resultCode == RESULT_OK){
                String email = (String) data.getSerializableExtra("email");
                if(account.contains(email)){
                    Toast.makeText(getApplicationContext(), "账号已登陆", Toast.LENGTH_LONG).show();
                    return;
                }
                account.add(email);
                User user = userManager.getUserByEmail(email)[0];
                String name = (String) data.getSerializableExtra("name");
                if(user.getAvatar().equals("")) {
                    header.addProfile(new ProfileDrawerItem().withName(name)
                        .withEmail(email).withIdentifier(identifier)
                        .withIcon(R.drawable.header), position);
                    user.setAvatar("http://166.111.5.239:8000/downloadImage/?filename=header.jpg");
                    userManager.updateUser(user);
                    asyncServerNews.asyncUserToServer(user);
                }
                else {
                    header.addProfile(new ProfileDrawerItem().withName(name)
                            .withEmail(email).withIdentifier(identifier)
                            .withIcon(user.getAvatar()), position);
                }
                header.setActiveProfile(identifier, true);
                header.updateProfile(header.getProfiles().get(identifier - 1));
                asyncServerNews.asyncDataFromServer();
                ++identifier;
                ++position;
            }
        } else if(requestCode == PICK_IMAGE){
//            try {
                if(data == null)
                    return;
//                try {
                    User user = userManager.getUserByEmail(header.getActiveProfile().getEmail().toString())[0];
//                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
//                            Uri.parse(PostImage.getRealPathFromURI(data.getData(), getContentResolver())));
//                    user.setAvatar(bitmap);
                    imagePoster.postAvaterToServer(ImageFilePath.getPath(this, data.getData()), user);
                    homepage.updateHeader(data.getData());
                    header.getActiveProfile().withIcon(data.getData());
                    header.updateProfile(header.getActiveProfile());

                    asyncServerNews.asyncUserToServer(user);
//                    System.out.println(getSupportFragmentManager().getFragments().size());
//                } /*catch (FileNotFoundException e){
//                    e.printStackTrace();
//                } catch (IOException e){
//                    e.printStackTrace();
//                }*/

//            } catch (FileNotFoundException e){
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "更换头像失败", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    private void overrideDrawerImageLoaderPicasso(){
        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.get().load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                super.set(imageView, uri, placeholder, tag);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.get().cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return super.placeholder(ctx);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                return super.placeholder(ctx, tag);
            }
        });
    }

    private void buildDrawer(final Activity activity){
        overrideDrawerImageLoaderPicasso();
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
        BaseDrawerItem item6 = new SecondaryDrawerItem().withIdentifier(FILTER_IDENTIFIER).withName("设置屏蔽词")
                .withIcon(R.drawable.ic_filter_list).withTextColor(Color.parseColor("#ababab"));
        BaseDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(SEARCH_IDENTIFIER).withName("查找好友")
                .withIcon(R.drawable.ic_search).withTextColor(Color.parseColor("#ababab"));
        SwitchDrawerItem switchDrawerItem = new SwitchDrawerItem().withIdentifier(NIGHT_IDENTIFIER).withName("夜间模式")
                .withIcon(R.drawable.night).withTextColor(Color.parseColor("#ababab")).withSelectable(false)
                .withOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                            setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        else
                            setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

//                        onSaveInstanceState(savedInstanceState);
                        Intent intent = new Intent(activity, Table.class);
                        String[] email = new String[header.getProfiles().size() - 2];
                        String[] name = new String[header.getProfiles().size() - 2];
                        int cnt = 0;
                        for(IProfile profile : header.getProfiles()){
                            if(profile.getIdentifier() < 3)
                                continue;
                            email[cnt] = profile.getEmail().toString();
                            name[cnt] = profile.getName().toString();
                            ++cnt;
                        }
//                        outState.putStringArray("email", email);
//                        outState.putStringArray("name", name);
//                        if(header.getProfiles().size() > 2)
//                            outState.putLong("Active ID", header.getActiveProfile().getIdentifier());
                        intent.putExtra("email", email);
                        intent.putExtra("name", name);
                        if(header.getProfiles().size() > 2)
                            intent.putExtra("Active ID", header.getActiveProfile().getIdentifier());
//                        finish();
//                        overridePendingTransition(R.xml.slide_no_move, R.xml.fade);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
//                        finish();
//                        recreate();
                    }
                });
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            switchDrawerItem.withChecked(true);



        header = new AccountHeaderBuilder()
                .withActivity(this)
//                .addProfiles(
//                        new ProfileDrawerItem().withName("Weize Chen").withIdentifier(3)
//                        .withEmail("chenweize@mails.tsinghua.edu.cn").withIcon(R.drawable.chenweize),
//                        new ProfileDrawerItem().withName("Hao Peng").withIdentifier(4)
//                                .withEmail("h-peng17@mails.tsinghua.edu.cn").withIcon(R.drawable.penghao)
//                )
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
                            userManagerOnServer.userSignOut();
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
//                            Intent intent = new Intent();
//                            intent.setType("image/*");
//                            intent.setAction(Intent.ACTION_GET_CONTENT);
//                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                            homepage = new PersonalHomepage(
                                    userManager.getUserByEmail(header.getActiveProfile().getEmail().toString())[0],
                                    fragmentManager
                            );
                            fragmentTransaction.replace(R.id.table_fragment, homepage, "Homepage");
                            if(fragmentManager.getBackStackEntryCount() == 0)
                                fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            fragmentTransaction.commit();
                            return false;
                        } else {
                            header.setActiveProfile(profile.getIdentifier(), true);
                            newsManager.deleteAllCollection();
                            newsManager.deleteAllHistory();
                            newsManager.resetWeightMap();
                            String name = header.getActiveProfile().getName().toString();
                            String email = header.getActiveProfile().getEmail().toString();
                            String passwd = userManager.getPassword(email);
                            userManagerOnServer.userSignIn(email, name, passwd);
                            asyncServerNews.asyncUserFromServer();
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
//        if(savedInstanceState != null){
//            String[] email = savedInstanceState.getStringArray("email");
//            String[] name = savedInstanceState.getStringArray("name");
//            for(int i = 0; i < email.length; ++i){
//                header.addProfiles(new ProfileDrawerItem().withName(name[i]).withIdentifier(3+i)
//                                        .withEmail(email[i]).withIcon(R.drawable.header));
//            }
//            if(header.getProfiles().size() > 2)
//                header.setActiveProfile(savedInstanceState.getLong("Active ID"));
//        }
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withSelectedItem(-1)
                .addDrawerItems(
                        item1,
                        item2,
                        item6,
                        item7,
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
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                            CollectionNews collectionNews = new CollectionNews(fragmentManager);
                            fragmentTransaction.replace(R.id.table_fragment, collectionNews);
                            if(fragmentManager.getBackStackEntryCount() == 0)
                                fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            fragmentTransaction.commit();

                        } else if (drawerItem.getIdentifier() == HISTORY_IDENTIFIER) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                            HistoryNews historyNews = new HistoryNews(fragmentManager);
                            fragmentTransaction.replace(R.id.table_fragment, historyNews);
                            if(fragmentManager.getBackStackEntryCount() == 0)
                                fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            fragmentTransaction.commit();
                        } else if (drawerItem.getIdentifier() == CLEAR_IDENTIFIER) {
                            spotsDialog = new SpotsDialog.Builder()
                                    .setContext(Table.this)
                                    .setCancelable(false)
                                    .setTheme(R.style.Clearing)
                                    .build();
                            spotsDialog.show();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                    spotsDialog.dismiss();
//                                    Toast.makeText(getApplicationContext(), "历史记录已清除", Toast.LENGTH_LONG).show();
                                }
                            }).start();
                            newsManager.deleteAllHistory();
                            mAdapterNews.notifyDataSetChanged();
                            drawer.setSelectionAtPosition(-1);


                        } else if (drawerItem.getIdentifier() == UPLOAD_IDENTIFIER) {
                            spotsDialog = new SpotsDialog.Builder()
                                    .setContext(Table.this)
                                    .setCancelable(false)
                                    .setTheme(R.style.Uploading)
                                    .build();
                            spotsDialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                    spotsDialog.dismiss();
                                }
                            }).start();

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        asyncServerNews.asyncDataToServer();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }  else if (drawerItem.getIdentifier() == DOWNLOAD_IDENTIFIER) {

                            spotsDialog = new SpotsDialog.Builder()
                                    .setContext(Table.this)
                                    .setCancelable(false)
                                    .setTheme(R.style.Downloading)
                                    .build();
                            spotsDialog.show();
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
                            asyncServerNews.asyncDataFromServer();

//                            newsManager.deleteAllHistory();
//                            asyncServerNews.asyncNewsFromServer();
//                            newsManager.deleteAllCollection();
//                            asyncServerNews.asyncCollectionNewsFromServer();
//                            newsManager.resetWeightMap();
//                            asyncServerNews.asyncWeightMapFromServer();
                            mAdapterNews.notifyDataSetChanged();

                        } else if(drawerItem.getIdentifier() == FILTER_IDENTIFIER) {
                            Intent intent = new Intent(getApplicationContext(), FilterWord.class);
                            startActivity(intent);
                            overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
                        } else if(drawerItem.getIdentifier() == SEARCH_IDENTIFIER) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                            FindFriend findFriend = new FindFriend(getSupportFragmentManager());
                            fragmentTransaction.replace(R.id.table_fragment, findFriend);
                            if(fragmentManager.getBackStackEntryCount() == 0 ||
                                    fragmentManager.getBackStackEntryCount() == 1) {
                                fragmentTransaction.addToBackStack(null);
                            }
//                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            fragmentTransaction.commit();
                        }
                        return false;
                    }
                })
                .build();
//        userManagerOnServer.userSignIn("wei10@mails.tsinghua.edu.cn", "Weize Chen", "123456");
    }

    @Override
    protected void onStop (){
        super.onStop();
        newsManager.resetRecommendation();
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        String[] email = new String[header.getProfiles().size() - 2];
//        String[] name = new String[header.getProfiles().size() - 2];
//        int cnt = 0;
//        for(IProfile profile : header.getProfiles()){
//            if(profile.getIdentifier() < 3)
//                continue;
//            email[cnt] = profile.getEmail().toString();
//            name[cnt] = profile.getName().toString();
//            ++cnt;
//        }
//        outState.putStringArray("email", email);
//        outState.putStringArray("name", name);
//        if(header.getProfiles().size() > 2)
//            outState.putLong("Active ID", header.getActiveProfile().getIdentifier());
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent){
//        String[] email = intent.getStringArrayExtra("email");
//        String[] name = intent.getStringArrayExtra("name");
//        if(email != null) {
//            for (int i = 0; i < email.length; ++i) {
//                header.addProfiles(new ProfileDrawerItem().withName(name[i]).withIdentifier(3 + i)
//                        .withEmail(email[i]).withIcon(R.drawable.header));
//            }
//            if (header.getProfiles().size() > 2)
//                header.setActiveProfile(intent.getLongExtra("Active ID", 1));
//        }
//    }

    @Override
    public void onResume(){
        super.onResume();
        if(mAdapterNews != null)
            mAdapterNews.notifyDataSetChanged();
        drawer.setSelectionAtPosition(-1);
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
//            newsManager.resetRecommendation();
            getSupportFragmentManager().popBackStack();
            drawer.setSelectionAtPosition(-1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        newsManager.deleteAllCollection();
//        newsManager.deleteAllHistory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}


