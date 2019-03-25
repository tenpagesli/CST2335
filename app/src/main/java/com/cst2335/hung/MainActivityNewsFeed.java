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


/*    static private String[] arrayString = new String[] { "Article 1", "Article2",
            "Article3", "Article4", "Article5", "Article6", "Article7", "Article8", "Article9",
            "Article10","Article11", "Article12", "Article13" };*/

    private ArrayAdapter<String> adapterString;
    private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news_feed);


        Button searchBtn = findViewById(R.id.search_btn);
        // clicked on search button
        searchBtn.setOnClickListener(c->{
            // search word from online, and jump to detailed page
            Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedSearches.class );
            startActivity(nextPage);
            //startActivityForResult(nextPage, 30);
        });

/*        adapterString = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayString);
        listView1 = findViewById(R.id.news_feed_list);
        listView1.setAdapter(adapterString);*/

        ArrayList<News> newsArrayList = new ArrayList<News>();
        newsArrayList.add(new News("Samsung battery explode", "djsaiudjasiudja", 1));
        newsArrayList.add(new News("Cheese found the moon", "djsaiudjasiudja", 2));
        newsArrayList.add(new News("Cracks found on butts", "djsaiudjasiudja", 3));

        NewsAdapter newsAdt = new NewsAdapter(newsArrayList, getApplicationContext());

        // Get a reference to the ListView, and attach the adapter to the listView.
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
