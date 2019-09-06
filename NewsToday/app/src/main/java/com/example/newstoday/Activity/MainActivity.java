package com.example.newstoday.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.newstoday.R;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    final String PREFS_NAME = "MyPrefsFile";
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
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        final boolean firstRun = settings.getBoolean("my_first_time", true);
//        ShimmerFrameLayout container = findViewById(R.id.shimmer_view_container);
//        container.startShimmer();
        Timer timer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
//                if(firstRun) {
//                    settings.edit().putBoolean("my_first_time", false).commit();
                startActivity(new Intent(getApplicationContext(), OpeningAnimation.class));
//                }
//                else
//                startActivity(new Intent(getApplicationContext(), Table.class));
                finish();
//                overridePendingTransition(R.xml.fade_in, R.xml.fade_out);
            }
        };
        timer.schedule(task, 3000);
    }
}
