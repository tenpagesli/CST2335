package com.cst2335.hung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.R;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;
import com.cst2335.ryan.MainActivityDictionary;
import com.cst2335.ryan.Word;


import java.util.ArrayList;

public class MainActivityNewsFeed extends AppCompatActivity {
    SharedPreferences sp;
    TextView inputWord;
    /** to save all the words */
    private ArrayList<News> newsList;
    /** to save word contents */
    private News news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news_feed);

        //search button
        Button searchBtn = findViewById(R.id.search_btn);
        inputWord = findViewById(R.id.edit_search);

        sp = getSharedPreferences("SearchedNews", Context.MODE_PRIVATE);
        String savedString = sp.getString("inputWord", "");
        inputWord.setText(savedString);

        searchBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedSearches.class );
            nextPage.putExtra("inputWord", inputWord.getText().toString());
            startActivity(nextPage);
            //startActivityForResult(nextPage, 30);
        });

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar_hd);
        setSupportActionBar(tBar);

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
                Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedSavedSearches.class );
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

    /**
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage = null;
        switch(item.getItemId())
        {
            //when click on "dictionary"
            case R.id.go_flight:
                nextPage = new Intent(MainActivityNewsFeed.this, MainActivityFlightStatusTracker.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_dic:
                nextPage = new Intent(MainActivityNewsFeed.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_new_york:
                nextPage = new Intent(MainActivityNewsFeed.this, MainActivityNewYorkTimes.class);
                startActivity(nextPage);
                break;



            // when click on "help":
            case R.id.go_help:
                // show help dialog
                showDialog();

                break;
        }
        return true;
    }

    /**
     * news feed toolbar inflater
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_newsfeed_hd, menu);
        return true;
    }

    /**
     * show help dialog of toolbar(overflow)
     */
    private void showDialog() {
        //pop up custom dialog to show Activity Version, Author, and how to use news feed
        View middle = getLayoutInflater().inflate(R.layout.activity_main_news_feed_help_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Activity Version: 1.0\n" +
                "Author: Hung\n" +
                "How to use: Enter search term of the article. Click on article for further details.")
/*                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })*/
                .setNegativeButton("OK.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "inputWord"
        String whatWasTyped = inputWord.getText().toString();
        // xml tag name is inputWord
        editor.putString("inputWord", whatWasTyped);

        //write it to disk:
        editor.commit();
    }
}
