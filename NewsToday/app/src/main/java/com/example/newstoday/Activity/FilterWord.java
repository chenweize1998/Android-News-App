package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.newstoday.CustomLayout.DragGridLayout;
import com.example.newstoday.NewsManager;
import com.example.newstoday.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.TreeMap;

public class FilterWord extends AppCompatActivity {
    private DragGridLayout gridLayout;
    private NewsManager newsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_word);

        newsManager = NewsManager.getNewsManager(getApplicationContext());

        gridLayout = findViewById(R.id.filter_grid_layout);
        gridLayout.setIsRemain(true);
        gridLayout.setCanDrag(false);

        ArrayList<String> words = new ArrayList<>(newsManager.getFilterWords());
        ListIterator<String> iterator = words.listIterator();
        while(iterator.hasNext()){
            String w = iterator.next();
            iterator.set("× " + w);
        }
        gridLayout.setItems(words);
        gridLayout.setColumnCount(4);

        gridLayout.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                newsManager.deleteFilterWord(tv.getText().toString().substring(2));
                gridLayout.removeView(tv);
            }
        });

        final TextInputEditText input = findViewById(R.id.filter_edit);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    if(i == KeyEvent.KEYCODE_ENTER){
                        String word = input.getText().toString();
                        if(word.equals("") || newsManager.hasFilterWord(word))
                            return false;
                        gridLayout.addGridItem("× " + input.getText().toString());
                        newsManager.addFilterWord(input.getText().toString());
                        input.setText("");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        newsManager.buildFilterWords();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
