package com.cst2335;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;
import com.cst2335.ryan.MainActivityDictionary;

public class MainActivity extends AppCompatActivity {

    Toolbar tBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        Button dicBtn = findViewById(R.id.enter_dic);
        Button fstBtn = findViewById(R.id.enter_fst);
        Button nfBtn = findViewById(R.id.enter_nf);
        Button nytBtn = findViewById(R.id.enter_nyt);
       // ImageButton nytMenuBtn = findViewById(R.id.nyt_menu_KN);


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
       // nytMenuBtn.setOnClickListener(c->{
//            Intent nextPage = new Intent(MainActivity.this, MainActivityNewYorkTimes.class);
//            startActivity(nextPage);
//        });
    }

    /**
     * inflate the icons for toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * when click on the icons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage = null;
        switch(item.getItemId())
        {
            //when click on "dictionary":
            case R.id.go_dic:
                nextPage = new Intent(MainActivity.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            // when click on "flight tracker":
            case R.id.go_flight:
                nextPage = new Intent(MainActivity.this, MainActivityFlightStatusTracker.class);
                startActivity(nextPage);
                break;
            // when click on "news feed":
            case R.id.go_news_feed:
                nextPage = new Intent(MainActivity.this, MainActivityNewsFeed.class);
                startActivity(nextPage);
                break;
            // when click on "new york times":
            case R.id.go_new_york:
                nextPage = new Intent(MainActivity.this, MainActivityNewYorkTimes.class);
                startActivity(nextPage);
                break;

            // when click on "help":
            case R.id.go_help:
                // show help dialog
                this.showDialog();
                break;
        }
        return true;
    }

    /**
     *  show the help dialog
     */
    private void showDialog(){
        View middle = getLayoutInflater().inflate(R.layout.activity_help_dialog, null);
        TextView authorName = (TextView)middle.findViewById(R.id.author_name);
        TextView versionNumber = (TextView)middle.findViewById(R.id.version_number);
        authorName.setText(Html.fromHtml(MyUtil.appAuther));
        versionNumber.setText(Html.fromHtml(MyUtil.appVersion));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", (DialogInterface dialog, int id) ->{

        }).setView(middle);
        builder.create().show();
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
