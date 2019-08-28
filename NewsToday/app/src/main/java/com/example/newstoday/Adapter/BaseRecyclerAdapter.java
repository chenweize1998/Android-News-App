package com.example.newstoday.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.News;
import com.example.newstoday.R;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class BaseRecyclerAdapter
        extends RecyclerView.Adapter<BaseRecyclerAdapter.MyViewHolder> {

    protected int mCount;
    private News mNews;
//    private int mWidth;
    private Context mContext;

    public BaseRecyclerAdapter(int count, Context context, News news) {
        mCount = count;
        mNews = news;
//        mWidth = width;
        mContext = context;
    }

    @NonNull @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MyViewHolder.createViewHolder(parent);
    }

    @Override public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bindView(position, mContext, mNews);
    }

    @Override public int getItemCount() {
        return mCount;
    }

    public void add() {
        int position = mCount;
        mCount++;
        notifyItemInserted(position);
    }

    public void remove() {
        if (mCount == 0) {
            return;
        }
        mCount--;
        int position = mCount;
        notifyItemRemoved(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

//        private final Random random = new Random();
        private ImageView image;

        static MyViewHolder createViewHolder(@NonNull ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_pager, parent, false);
            return new MyViewHolder(v);
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.page_image);
        }

        void bindView(final int position, final Context context, final News news) {
//            TextView textView = (TextView) itemView;
//            textView.setText(String.valueOf(position + 1));
//            textView.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
            if(news.getImage()[0] != "") {
                Picasso.get().load(news.getImage()[position]).fit().centerCrop().into(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Dialog builder = new Dialog(context);
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        builder.getWindow().setBackgroundDrawable(
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                            }
                        });

                        ImageView imageView = new ImageView(context);
                        Picasso.get().load(news.getImage()[position]).into(imageView);
                        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                        builder.show();
                        builder.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    }
                });
            }
        }
    }
}