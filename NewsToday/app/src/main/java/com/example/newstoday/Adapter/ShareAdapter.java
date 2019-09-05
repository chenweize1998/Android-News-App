package com.example.newstoday.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.Activity.Login;
import com.example.newstoday.Activity.Table;
import com.example.newstoday.ForwordingNewsManager;
import com.example.newstoday.News;
import com.example.newstoday.R;
import com.example.newstoday.WechatShareManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.MyViewHolder> {
    private String[] text = {"朋友圈", "微信好友", "微博", "QQ", "QQ空间", "转到动态"};
    private int[] icon = {R.drawable.moment, R.drawable.wechat, R.drawable.weibo,
                            R.drawable.qq, R.drawable.qzone, R.mipmap.ic_launcher_round};
    private ForwordingNewsManager forwordingNewsManager;
    private News news;
//    private Context context;
    private Activity activity;

    public static final int LOGIN_REQUEST = 2;

    WechatShareManager wechatShareManager;

    public ShareAdapter(News news, Activity activity){
        this.activity = activity;
//        this.context = context;
        this.news = news;
        wechatShareManager = WechatShareManager.getInstance(activity.getApplicationContext());
        forwordingNewsManager = ForwordingNewsManager.getForwordingNewsManager(activity.getApplicationContext());
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        public MyViewHolder(View v) {
            super(v);
            imageView = v.findViewById(R.id.share_icon);
            textView = v.findViewById(R.id.share_text);
        }
    }

    @Override
    public ShareAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_item, parent, false);
        ShareAdapter.MyViewHolder vh = new ShareAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ShareAdapter.MyViewHolder holder, final int position) {
        holder.imageView.setImageResource(icon[position]);
        holder.textView.setText(text[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 1){
                    wechatShareManager.shareNews(news);
                } else if(position == 5){
                    if(Table.header.getActiveProfile() == null){
                        Toast.makeText(activity.getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity.getApplicationContext(), Login.class);
                        activity.startActivityForResult(intent, LOGIN_REQUEST);
                        return;
                    }
                    forwordingNewsManager.addOneForwardingNewsForUser(news,
                            Table.header.getActiveProfile().getEmail().toString());
                    activity.finish();
                    Toast.makeText(activity.getApplicationContext(), "转发成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return text.length;
    }
}
