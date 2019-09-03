package com.example.newstoday.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.content.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.Activity.BottomSheetDialog;
import com.example.newstoday.Activity.Table;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.OfflineNewsManager;
import com.example.newstoday.R;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    private ArrayList<News> news;
    private Pattern pat;
    private OnItemClickListener listener;
    private NewsManager newsManager;
    private OfflineNewsManager offlineNewsManager;
    private Activity activity;
    private BottomSheetDialog bottomSheetDialog;
    private FragmentManager fragmentManager;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtAbstract;
        private ImageView imgNews;
        private TextView txtKeyword;
        private ImageButton shareButton;
//        private ImageButton starButton;
        private ShineButton starButton;
        private ImageView cover;
        public MyViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtAbstract = v.findViewById(R.id.txtAbstract);
            imgNews = v.findViewById(R.id.imgNews);
            txtKeyword = v.findViewById(R.id.item_keyword);
            shareButton = v.findViewById(R.id.item_share_button);
            starButton = v.findViewById(R.id.item_star_button);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, final View v);
    }

    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public NewsAdapter(ArrayList<News> news, Activity activity, FragmentManager fragmentManager) {
        this.news = news;
        pat = Pattern.compile("[！？。…~]");
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    public void updateNews(ArrayList<News> news){
        this.news = news;
    }

    public void refreshNews(ArrayList<News> news){
        this.news.addAll(news);
    }

    public void updateActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        newsManager = NewsManager.getNewsManager(parent.getContext());
        offlineNewsManager = OfflineNewsManager.getOfflineNewsManager(parent.getContext());
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        Typeface typefaceAbstract = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "font/siyuanlight.otf");
        holder.txtTitle.setText(news.get(position).getTitle().replace((char)12288+"", ""));
//        holder.txtTitle.setTypeface(typefaceTitle);
        String tmp = news.get(position).getContent()
                .replace((char)12288+"", "").replace("\n", "");
        tmp = pat.split(tmp)[0];
        holder.txtAbstract.setText(tmp);
//        holder.txtAbstract.setTypeface(typefaceAbstract);     # 看看到时候要不要设置字体
        holder.txtKeyword.setText(news.get(position).getKeywords()[0]);

//        System.out.println("现在的position"+position);
        if(!news.get(position).getImage()[0].equals("")) {
            Picasso.get().load(news.get(position).getImage()[0]).into(holder.imgNews);
        }
        else
            holder.imgNews.setImageResource(0);

        if(newsManager.inCollectionNews(news.get(position)))
            holder.starButton.setChecked(true);
        else
            holder.starButton.setChecked(false);

        if(newsManager.inHistoryNews(news.get(position))) {
            holder.txtTitle.setTextColor(ContextCompat.getColor(holder.txtTitle.getContext(), R.color.titleItemSelColor));
        }
        else {
            holder.txtTitle.setTextColor(ContextCompat.getColor(holder.txtTitle.getContext(), R.color.titleItemUnselColor));
        }
        holder.starButton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                News tmp = news.get(position);
                if(newsManager.inCollectionNews(tmp)){
                    newsManager.deleteOneCollection(tmp);
//                    holder.starButton.setImageResource(R.drawable.not_star);
                } else{
                    newsManager.addInCollection(tmp);
//                    holder.starButton.setImageResource(R.drawable.star_selected);
                }
            }
        });

        final FragmentManager fragmentManager = this.fragmentManager;
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                wsm.shareNews(news.get(position));
                bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                News tmp = news.get(position);
                String[] keywords = news.get(position).getKeywords();
                Double[] scores = news.get(position).getDoubleScores();
                for(int i = 0; i < keywords.length; ++i) {
                    if (scores[i] < 0.5 || newsManager.inHistoryNews(tmp)) {
                        break;
                    }
                    newsManager.addWeight(scores[i], keywords[i]);
                }
                newsManager.addInHistory(tmp);
                offlineNewsManager.addOneOfflineNews(tmp);
                holder.txtTitle.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.titleItemSelColor));
                listener.onItemClick(position, v);
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