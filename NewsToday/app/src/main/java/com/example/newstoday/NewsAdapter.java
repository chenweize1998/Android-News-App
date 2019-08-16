package com.example.newstoday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private String[] title;
    private String[] abs;
    private ArrayList<News> news;
    private Context context;
    private Pattern pat;
    private OnItemClickListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtAbstract;
        private ImageView imgNews;
        private TextView txtKeyword;
        public MyViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtAbstract = v.findViewById(R.id.txtAbstract);
            imgNews = v.findViewById(R.id.imgNews);
            txtKeyword = v.findViewById(R.id.item_keyword);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener) {
        this.listener = listener;
        pat = Pattern.compile("[！？。…~]");
    }

    public NewsAdapter(ArrayList<News> news) {
        this.news = news;
    }

    public void updateNews(ArrayList<News> news){
        this.news = news;
    }

    public void refreshNews(ArrayList<News> news){
        this.news.addAll(news);
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.txtTitle.setText(title[position]);
//        holder.txtAbstract.setText(abs[position]);
        holder.txtTitle.setText(news.get(position).getTitle().replace((char)12288+"", ""));
        String tmp = news.get(position).getContent()
                .replace((char)12288+"", "").replace("\n", "");
//        tmp = tmp.substring(0, tmp.length() < 35? tmp.length():35) + "...";
        tmp = pat.split(tmp)[0];
//        if(tmp.length() > 30)
//            tmp = tmp.substring(0, 30);
        tmp = tmp + "。...";
        holder.txtAbstract.setText(tmp);
        holder.txtKeyword.setText(news.get(position).getKeywords()[0]);
        if(!news.get(position).getImage()[0].equals(""))
            Picasso.get().load(news.get(position).getImage()[0]).into(holder.imgNews);
        else
            holder.imgNews.setImageResource(R.mipmap.default_pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listener.onItemClick(position);
            }
        });
    }



    @Override
    public int getItemCount() {
        return news.size();
    }
}

//class DownLoadImageTask extends AsyncTask<String,Void,Bitmap>{
//    private ImageView view;
////        private Bitmap bm;
//
//    public DownLoadImageTask(ImageView view){
//        this.view = view;
//    }
//
//    protected Bitmap doInBackground(String...urls){
//        //        System.out.println(urls[0]);
//        //        String[] urlOfImage = urls[0].split(",");
//        //        String urlText = urlOfImage[0];
//        //        if (urlOfImage.length > 1)
//        //            urlText = urlText.substring(1, urlText.length());
//        //        else
//        //            urlText = urlText.substring(1, urlText.length()-1);
//        //        if(urlText == "")
//        //            return null;
//        String urlText = urls[0];
//        Bitmap bimage = null;
//        try{
//            URL url = new URL(urlText);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            bimage = BitmapFactory.decodeStream(input);
//            return bimage;
//        }catch(Exception e){
//            e.printStackTrace();
//            //            System.out.println("Error:"+urlText);
//        }
//        return bimage;
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        view.setImageBitmap(result);
//    }
//}