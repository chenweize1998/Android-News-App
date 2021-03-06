package com.example.newstoday.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newstoday.Adapter.ShareAdapter;
import com.example.newstoday.News;
import com.example.newstoday.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private News news;

    public BottomSheetDialog(){}

    public BottomSheetDialog(News news){
        this.news = news;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);

//        ImageButton button1 = v.findViewById(R.id.wechat);
//        ImageButton button2 = v.findViewById(R.id.weibo);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        RecyclerView recyclerView = v.findViewById(R.id.share_recycler);
        RecyclerView.LayoutManager layoutManagerNews = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerNews);
        ShareAdapter mAdapter = new ShareAdapter(news, getActivity(), this);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

}
