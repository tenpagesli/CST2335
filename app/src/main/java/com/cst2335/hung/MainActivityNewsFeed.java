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


import java.util.ArrayList;

/**
 * Author: Hung Doan
 * Version: 1.4
 * Last modified: 2019-04-15
 */
public class MainActivityNewsFeed extends AppCompatActivity {
    SharedPreferences sp; //edit search shared prefs
    TextView editSearch; //edit search view

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news_feed);

        Button searchBtn = findViewById(R.id.search_btn); //search button
        Button saveBtn = findViewById(R.id.saved_btn); //save button
        editSearch = findViewById(R.id.edit_search); //type in your search edit text

        //edit text shared preferences
        sp = getSharedPreferences("searchedArticle", Context.MODE_PRIVATE);
        String savedString = sp.getString("savedSearch", "");
        editSearch.setText(savedString);

        //search button on click
        searchBtn.setOnClickListener(c -> {
            Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedSearches.class);
            nextPage.putExtra("searchedArticle", editSearch.getText().toString());
            startActivity(nextPage);
        });

        //toolbar containing team member's activities
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar_hd);
        setSupportActionBar(tBar);

        //save button onclick
        saveBtn.setOnClickListener(d -> {
            Intent nextPage = new Intent(MainActivityNewsFeed.this, NewsFeedSavedArticles.class);
            startActivity(nextPage);

        });

    }

    /**
     * toolbar leading to different teammate projects
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage = null;
        switch (item.getItemId()) {
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
     * news feed toolbar inflate
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

        builder.setMessage("Activity Version: 1.4\n" +
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

    /**
     * shared prefs for search edit text
     */
    @Override
    protected void onPause() {
        super.onPause();
        //get an editor object
        SharedPreferences.Editor editor = sp.edit();
        //save what was typed under the name "editSearch"
        String whatWasTyped = editSearch.getText().toString();
        editor.putString("savedSearch", whatWasTyped);
        //write it to disk:
        editor.commit();
    }
}
