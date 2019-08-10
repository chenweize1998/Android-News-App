package com.example.newstoday;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] title;
    private String[] abs;

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
    public MyAdapter(String[] title, String[] abs) {
        this.title = title;
        this.abs = abs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
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
        holder.txtTitle.setText(title[position]);
        holder.txtAbstract.setText(abs[position]);
        holder.imgNews.setImageResource(R.mipmap.front_page);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return title.length;
    }
}