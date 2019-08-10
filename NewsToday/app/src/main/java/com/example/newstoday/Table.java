package com.example.newstoday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//import com.example.newstoday.ui.table.TableFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

public class Table extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] title, abs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);

        title = new String[20];
        abs = new String[20];
        for(int i = 0; i < 20; ++i)
        {
            title[i] = "Title" + i;
            abs[i] = "Abstract" + i;
        }

        recyclerView = findViewById(R.id.table_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MyAdapter(title, abs);
        recyclerView.setAdapter(mAdapter);
    }
}