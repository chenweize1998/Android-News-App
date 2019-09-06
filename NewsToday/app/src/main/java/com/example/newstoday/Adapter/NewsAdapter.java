package com.example.newstoday.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newstoday.Activity.BottomSheetDialog;
import com.example.newstoday.News;
import com.example.newstoday.NewsManager;
import com.example.newstoday.OfflineNewsManager;
import com.example.newstoday.R;
import com.example.newstoday.User;
import com.example.newstoday.UserManager;
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
    private UserManager userManager;
    private boolean isHomePage;
    private boolean isPersonalHomePage;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtAbstract;
        private ImageView imgNews;
        private TextView txtKeyword;
        private ImageButton shareButton;
        private ShineButton starButton;
        private CardView header_layout;
        private ImageView header;
        public MyViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtTitle);
            txtAbstract = v.findViewById(R.id.txtAbstract);
            imgNews = v.findViewById(R.id.imgNews);
            txtKeyword = v.findViewById(R.id.item_keyword);
            shareButton = v.findViewById(R.id.item_share_button);
            starButton = v.findViewById(R.id.item_star_button);
            header_layout = v.findViewById(R.id.item_header_container);
            header = v.findViewById(R.id.item_publisher_header);
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
        this.userManager = UserManager.getUserManager(activity.getApplicationContext());
    }

    public NewsAdapter(ArrayList<News> news, Activity activity,
                       FragmentManager fragmentManager, boolean isHomePage, boolean isPersonalHomePage) {
        this.news = news;
        pat = Pattern.compile("[！？。…~]");
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.userManager = UserManager.getUserManager(activity.getApplicationContext());
        this.isHomePage = isHomePage;
        this.isPersonalHomePage = isPersonalHomePage;
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
        newsManager = NewsManager.getNewsManager(parent.getContext());
        offlineNewsManager = OfflineNewsManager.getOfflineNewsManager(parent.getContext());
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtTitle.setText(news.get(position).getTitle().replace((char)12288+"", ""));
        String tmp = news.get(position).getContent()
                .replace((char)12288+"", "").replace("\n", "");
        tmp = pat.split(tmp)[0];
        holder.txtAbstract.setText(tmp);
        if(news.get(position).getKeywords() != null && news.get(position).getKeywords().length != 0)
            holder.txtKeyword.setText(news.get(position).getKeywords()[0]);
        else
            holder.txtKeyword.setVisibility(View.GONE);

        if(news.get(position).getCategory().equals("关注") && !isHomePage) {
//            holder.imgNews.setImageResource(0);
            holder.header_layout.setVisibility(View.VISIBLE);
            User publisher = userManager.getUserByEmail(news.get(position).getPublisher())[0];
//            String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), publisher.getAvatar(), "Header", null);
//            Picasso.get().load(Uri.parse(path)).into(holder.header);
        }
        else
            holder.header_layout.setVisibility(View.GONE);

        if(news.get(position).getImage().length!=0 && !news.get(position).getImage()[0].equals("")) {
//            Picasso.get().load(news.get(position).getImage()[0]).into(holder.imgNews);
//            holder.header_layout.setVisibility(View.GONE);
            Glide.with(activity).load(news.get(position).getImage()[0]).into(holder.imgNews);
        }
        else {
            holder.imgNews.setImageResource(0);
//            holder.header_layout.setVisibility(View.GONE);
        }
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
                } else{
                    newsManager.addInCollection(tmp);
                }
            }
        });

        final FragmentManager fragmentManager = this.fragmentManager;
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                bottomSheetDialog = new BottomSheetDialog(news.get(position));
                bottomSheetDialog.show(fragmentManager, "bottomSheet");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                News tmp = news.get(position);
                if(!newsManager.getLastCategory().equals("关注")) {
                    String[] keywords = news.get(position).getKeywords();
                    if(keywords != null && keywords.length == 0) {
                        Double[] scores = news.get(position).getDoubleScores();
                        for (int i = 0; i < keywords.length; ++i) {
                            if (scores[i] < 0.5 || newsManager.inHistoryNews(tmp)) {
                                break;
                            }
                            newsManager.addWeight(scores[i], keywords[i]);
                        }
                    }
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