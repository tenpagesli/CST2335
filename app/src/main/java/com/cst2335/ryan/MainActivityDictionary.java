/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user wants go to dictionary's home page, this file runs
 * **/
package com.cst2335.ryan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityDictionary extends AppCompatActivity {
    /** to save the word which the user typed in */
    SharedPreferences sp;
    /** the input word view */
    TextView inputWord;
    /** tool bar */
    Toolbar tBar;
    /** to save all the words */
    private ArrayList<Word> wordsList;
    /** to save word contents */
    private Word word;

    /***
     * this method runs when jump to main activiety dictonary page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dictionary);

        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        // get all the views from page
        inputWord = findViewById(R.id.search_input);
        Button searchBtn = findViewById(R.id.search_btn);
        Button viewSavedWords = findViewById(R.id.view_words_list);

        // get saved SharedPreference from disk, the saved file name is "SearchedWords"
        sp = getSharedPreferences("SearchedWords", Context.MODE_PRIVATE);
        // get the value from xml tag of <inputWord>
        String savedString = sp.getString("inputWord", "");
        inputWord.setText(savedString);

        // clicked on search button
        searchBtn.setOnClickListener(c->{
            // search word from online, and jump to detailed page
            Intent nextPage = new Intent(MainActivityDictionary.this, WordsDetailsActivity.class );
            nextPage.putExtra("inputWord", inputWord.getText().toString());
            startActivity(nextPage);
        });

        // clicked on view saved words button, jump to saved words list page
        viewSavedWords.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityDictionary.this, ViewSavedWordsActivity.class );
            startActivity(nextPage);
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
        // Snackbar code:
        Snackbar sb = Snackbar.make(tBar, "Do you want to switch to other module?", Snackbar.LENGTH_LONG)
                .setAction("Yes", e -> {
                    Intent nextPage = null;
                    switch(item.getItemId())
                    {
                        //when click on "dictionary"
                        case R.id.go_flight:
                            nextPage = new Intent(MainActivityDictionary.this, MainActivityFlightStatusTracker.class);
                            startActivity(nextPage);
                            break;
                        //when click on "news feed"
                        case R.id.go_news_feed:
                            nextPage = new Intent(MainActivityDictionary.this, MainActivityNewsFeed.class);
                            startActivity(nextPage);
                            break;
                        //when click on "new york times"
                        case R.id.go_new_york:
                            nextPage = new Intent(MainActivityDictionary.this, MainActivityNewYorkTimes.class);
                            startActivity(nextPage);
                            break;

                        // when click on "help":
                        case R.id.go_help:
                            // show help dialog
                            this.showDialog();
                            break;
                    }
                });
        sb.show();
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
        builder.setPositiveButton("OK", (DialogInterface dialog, int id) ->{

        }).setView(middle);
        builder.create().show();
    }

    /**
     * override onPause() for sharedPreference
     */
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
