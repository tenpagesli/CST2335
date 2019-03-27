/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.ryan.MainActivityDictionary;

public class FlightDetailsActivity extends AppCompatActivity {

    /** toolbar  */
    Toolbar tBar;

    /**
     * When user want's to see the flight details, this method runs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);
        // get toolbar
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        Button saveFlight = findViewById(R.id.save_btn);
        // when click on save button
        saveFlight.setOnClickListener(c->{
            Toast.makeText(FlightDetailsActivity.this, "The flight has already saved", Toast.LENGTH_LONG).show();
        });
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
        inflater.inflate(R.menu.toolbar_menu_flight_main_qy, menu);
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
            //when click on "dictionary"
            case R.id.go_dic:
                nextPage = new Intent(FlightDetailsActivity.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_news_feed:
                nextPage = new Intent(FlightDetailsActivity.this, MainActivityNewsFeed.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_new_york:
                nextPage = new Intent(FlightDetailsActivity.this, MainActivityNewYorkTimes.class);
                startActivity(nextPage);
                break;
            // when click on "help":
            case R.id.go_help:
                // show help dialog
                View middle = getLayoutInflater().inflate(R.layout.activity_help_dialog, null);
                TextView authorName = (TextView)middle.findViewById(R.id.author_name);
                TextView versionNumber = (TextView)middle.findViewById(R.id.version_number);
                TextView instructions = (TextView)middle.findViewById(R.id.instructions);
                authorName.setText(MyUtil.flightAuther);
                versionNumber.setText(MyUtil.versionNumber);
                instructions.setText(MyUtil.flightInstruction);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                        .setView(middle);

                builder.create().show();
                break;
        }
        return true;
    }
}
