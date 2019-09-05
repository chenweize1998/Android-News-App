package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.newstoday.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
//        ShimmerFrameLayout container = findViewById(R.id.shimmer_view_container);
//        container.startShimmer();
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
//                startActivity(new Intent(getApplicationContext(), Table.class));
                startActivity(new Intent(getApplicationContext(), Test.class));
                finish();
            }
        };
        timer.schedule(task, 3000);
    }

}
