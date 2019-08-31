package com.example.newstoday.Adapter;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.News;
import com.example.newstoday.R;
import com.squareup.picasso.Picasso;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.MyViewHolder> {
    private String[] text = {"朋友圈", "微信好友", "微博", "QQ", "QQ空间", "复制链接"};
    private int[] icon = {R.drawable.moment, R.drawable.wechat, R.drawable.weibo,
                            R.drawable.qq, R.drawable.qzone, R.drawable.copy};

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

            }
        });
    }

    @Override
    public int getItemCount() {
        return text.length;
    }
}
