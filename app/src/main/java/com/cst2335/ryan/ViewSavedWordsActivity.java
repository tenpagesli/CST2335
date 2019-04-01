/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user clicked on saved word list, this file runs
 * **/
package com.cst2335.ryan;

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
import android.widget.TextView;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;

public class ViewSavedWordsActivity extends AppCompatActivity {

    /** tool bar */
    Toolbar tBar;

    /**
     *  this method runs when click on "view saved word list"
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_view_saved_words);

        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

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
        inflater.inflate(R.menu.toolbar_menu_dictionary_rl, menu);
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
            case R.id.go_flight:
                nextPage = new Intent(ViewSavedWordsActivity.this, MainActivityFlightStatusTracker.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_news_feed:
                nextPage = new Intent(ViewSavedWordsActivity.this, MainActivityNewsFeed.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_new_york:
                nextPage = new Intent(ViewSavedWordsActivity.this, MainActivityNewYorkTimes.class);
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
        TextView instructions = (TextView)middle.findViewById(R.id.instructions);
        authorName.setText(MyUtil.dictionaryAuther);
        versionNumber.setText(MyUtil.versionNumber);
        instructions.setText(MyUtil.dictionaryInstruction);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        })
                .setView(middle);

        builder.create().show();
    }

}
