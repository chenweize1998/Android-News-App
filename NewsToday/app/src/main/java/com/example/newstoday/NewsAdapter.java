package com.example.newstoday;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import java.net.URL;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private String[] title;
    private String[] abs;
    private News[] news;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView txtTitle;
        private TextView txtAbstract;
        private ImageView imgNews;
        public MyViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtAbstract = v.findViewById(R.id.txtAbstract);
            imgNews = v.findViewById(R.id.imgNews);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(News[] news) {
//        this.title = title;
//        this.abs = abs;
        this.news = news;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.txtTitle.setText(title[position]);
//        holder.txtAbstract.setText(abs[position]);
        holder.txtTitle.setText(news[position].getTitle().replace((char)12288+"", ""));
        holder.txtAbstract.setText(news[position].getContent().replace((char)12288+"", "").substring(0, 30));
//        holder.imgNews.setImageResource(R.mipmap.front_page);
        if(news[position].getImage() != null)
//            Picasso.get().load(news[position].getImage()).into(holder.imgNews);
        {
//            Bitmap bm = Bitmap.createScaledBitmap(news[position].getImage(), 70, 40, true);
//            holder.imgNews.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.imgNews.setImageBitmap(news[position].getImage());
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return news.length;
    }
}