package com.example.newstoday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.newstoday.ui.table.TableFragment;

public class Table extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TableFragment.newInstance())
                    .commitNow();
        }
    }
}
