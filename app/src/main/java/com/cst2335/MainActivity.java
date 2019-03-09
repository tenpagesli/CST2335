package com.cst2335;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;
import com.cst2335.ryan.MainActivityDictionary;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button dicBtn = findViewById(R.id.enter_dic);
        Button fstBtn = findViewById(R.id.enter_fst);
        Button nfBtn = findViewById(R.id.enter_nf);
        Button nytBtn = findViewById(R.id.enter_nyt);
        // when user click on enter dictionary button
        dicBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, MainActivityDictionary.class);
            startActivity(nextPage);
        });
        // when user click on enter flight status tracker button
        fstBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, MainActivityFlightStatusTracker.class);
            startActivity(nextPage);
        });
        // when user click on enter news feed button
        nfBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, MainActivityNewsFeed.class);
            startActivity(nextPage);
        });
        // when user click on enter new york times article button
        nytBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, MainActivityNewYorkTimes.class);
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
