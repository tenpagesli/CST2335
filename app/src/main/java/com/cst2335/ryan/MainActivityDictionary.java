/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user wants go to dictionary's home page, this file runs
 * **/
package com.cst2335.ryan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityDictionary extends AppCompatActivity {

    private ArrayList<Word> wordsList;
    private Word word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dictionary);
        // get all the user input word
        TextView inputWord = findViewById(R.id.search_input);
        Button searchBtn = findViewById(R.id.search_btn);
        Button viewSavedWords = findViewById(R.id.view_words_list);
        Button refreshList = findViewById(R.id.refresh_list);
        ListView preWordsList = findViewById(R.id.words_list);

        // display the most recent 20 words that were searched
        wordsList = new ArrayList<>();
        // TODO: get the top 20 words from shared-prefference, and add them into words list
        // get the top 20 words from shared-preferrence
        ArrayList<String> definList = new ArrayList<String>();
        definList.add("very glad");
        wordsList.add(new Word("happy", definList, "adv", "I feel happy"));
        // get the adapter to inflate the most recent words list
        ListAdapter adt = new WordsListAdapter(wordsList);
        preWordsList.setAdapter(adt);
        if(!wordsList.isEmpty()){
            ((WordsListAdapter) adt).notifyDataSetChanged();
        }

        // clicked on search button
        searchBtn.setOnClickListener(c->{
            // search word from online, and jump to detailed page
            Intent nextPage = new Intent(MainActivityDictionary.this, WordsDetailsActivity.class );
            startActivity(nextPage);
        });

        // clicked on view saved words button, jump to saved words list page
        viewSavedWords.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityDictionary.this, ViewSavedWordsActivity.class );
            startActivity(nextPage);
        });

        // clicked on refresh words button, refresh previous searched words list
        refreshList.setOnClickListener(c->{
            // Snackbar code:
            Snackbar sb = Snackbar.make(viewSavedWords, "Do you want to refresh previous searched list?", Snackbar.LENGTH_LONG)
                    .setAction("Yes", e -> {
                        if(!wordsList.isEmpty()){
                            ((WordsListAdapter) adt).notifyDataSetChanged();
                        }
                    });
            sb.show();
        });

        // clicked on one item of the previous words, jump to the detail page of that word
        preWordsList.setOnItemClickListener((parent, view, position, id)->{
            Intent nextPage = new Intent(MainActivityDictionary.this, WordsDetailsActivity.class );
            startActivity(nextPage);
        });
    }

    //This class needs 4 functions to work properly:
    protected class WordsListAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //Keep a reference to the data:
        public WordsListAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //You can give it an array
        public WordsListAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }

        public WordsListAdapter() {
        }

        // return how many items to display
        @Override
        public int getCount() {
            return dataCopy.size();
        }

        // return the contents will show up in each row
        @Override
        public E getItem(int position) {
            return dataCopy.get(position);
        }


        /***
         *  this method set up and add the view that will be added to the bottom of the view list.
         *  Thsi method will be run list.size() times
         *   @param position: locates the one that will be add to the bottom
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            word = (Word)getItem(position);
            newView = inflater.inflate(R.layout.activity_pre_words, parent, false);
            TextView content = (TextView) newView.findViewById(R.id.pre_word);
            //Get the string to go in row: position
            String toDisplay = ((Word) getItem(position)).getWord();
            //Set the text of the text view
            content.setText(toDisplay);
            return newView;
        }

        // get the item id for a specific position in the view list.
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
