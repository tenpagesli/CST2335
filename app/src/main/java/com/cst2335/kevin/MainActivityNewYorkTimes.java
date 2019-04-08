/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * Description: shows the main menu and the latest articles with search bar
 * **/

package com.cst2335.kevin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;

import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.queeny.MainActivityFlightStatusTracker;
import com.cst2335.ryan.MainActivityDictionary;


import java.util.ArrayList;


public class MainActivityNewYorkTimes extends AppCompatActivity {

    private ArrayAdapter<String> adapterString;
    private ListView listView;
    private ImageButton androidImageButton;
    TextView inputWord;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_york_times);


        inputWord = findViewById(R.id.nyt_searchEdit);
        Button searchBtn = findViewById(R.id.nyt_searchButton);
        Button viewSaveArticle = findViewById(R.id.nyt_view_saved);
        androidImageButton = findViewById(R.id.nyt_imageBut);
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar_kn);
        setSupportActionBar(tBar);

        sp = getSharedPreferences("searchedArticle", Context.MODE_PRIVATE);
        // get the value from xml tag of <inputWord>
        String savedString = sp.getString("inputWord", "");
        inputWord.setText(savedString);

        // clicked on search button to go to new pages with searched results

        searchBtn.setOnClickListener(c -> {
            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytApiSearch.class);
            nextPage.putExtra("inputWord", inputWord.getText().toString());
            startActivity(nextPage);
        });

        viewSaveArticle.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytSavedArticle.class );
            startActivity(nextPage);
        });

        //click on image to go back to main menu
        androidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sb = Snackbar.make(androidImageButton, "Welcome To NewYork Times", Snackbar.LENGTH_LONG)
                        .setAction("Go Back To Main Menu?", e -> finish());
                sb.show();
            }
        });



        ArrayList<Article> newsArrayList = new ArrayList<Article>();
        newsArrayList.add(new Article("NYT Article 1 Test", "NYT 2019", 1));

        ArticleAdapter artAdt = new ArticleAdapter(newsArrayList, getApplicationContext());


        ListView listV = findViewById(R.id.nyt_newsList);
        listV.setClickable(true);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //populates article one from arraylist
                if (position == 0) {
                    Intent nextPage = new Intent(MainActivityNewYorkTimes.this, ArticleActivity1.class);
                    Log.i("ListView clicked: ", "0");
                    startActivity(nextPage);
                }
            }
        });
        listV.setAdapter(artAdt);
        /*
            this method will populates the listview will Arraylist objects
            returns article to listview

            to do, link to the website and return nyt Articles
        */
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        Intent nextPage = null;
        switch (item.getItemId()) {
//when click on "dictionary"
            case R.id.go_flight:
                nextPage = new Intent(MainActivityNewYorkTimes.this, MainActivityFlightStatusTracker.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_dic:
                nextPage = new Intent(MainActivityNewYorkTimes.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_news_feed:
                nextPage = new Intent(MainActivityNewYorkTimes.this, MainActivityNewsFeed.class);
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
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_newsfeed_hd, menu);
        return true;
    }

    /**
     * show help dialog of toolbar(overflow)
     */
    private void showDialog () {
        //pop up custom dialog to show Activity Version, Author, and how to use news feed
        View middle = getLayoutInflater().inflate(R.layout.activity_main_news_feed_help_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Activity Version: 1.0\n" +
                "Author: Kevin\n" +
                "How to use: Enter search term of the article. Click on article for further details.")
             /*    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

    }//end class

