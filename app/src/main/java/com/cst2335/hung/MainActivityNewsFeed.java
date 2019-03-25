package com.cst2335.hung;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst2335.MainActivity;
import com.cst2335.R;

import java.util.ArrayList;

public class MainActivityNewsFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news_feed);

        //search button
        Button searchBtn = findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedSearches.class );
            startActivity(nextPage);
            //startActivityForResult(nextPage, 30);
        });


        //arraylist of articles
        ArrayList<News> newsArrayList = new ArrayList<News>();
        newsArrayList.add(new News("Samsung battery explode", "djsaiudjasiudja", 1));
        newsArrayList.add(new News("Cheese found the moon", "djsaiudjasiudja", 2));
        newsArrayList.add(new News("Cracks found on butts", "djsaiudjasiudja", 3));

        NewsAdapter newsAdt = new NewsAdapter(newsArrayList, getApplicationContext());

        //list view of article set to clickable
        ListView lv = (ListView) findViewById(R.id.news_feed_list);
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*
                Object o = lv.getItemAtPosition(position);
                Toast.makeText(MainActivityNewsFeed.this, newsArrayList.get(position)+"", Toast.LENGTH_SHORT).show();
                Log.i("Item clicked", "yay");
                TextView v = (TextView) view.findViewById(R.id.news_title);
                Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
*/
            if (position == 0){
                Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedDetailed.class );
                Log.i("ListView clicked: ", "0");
                startActivity(nextPage);
            }
            if (position == 1) {
                Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedDetailed2.class);
                Log.i("ListView clicked", "1");
                startActivity(nextPage);
            }
            if (position == 2) {
                Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedDetailed3.class);
                Log.i("ListView clicked", "2");
                startActivity(nextPage);
                }

            }
        });
        lv.setAdapter(newsAdt);


    }

}
