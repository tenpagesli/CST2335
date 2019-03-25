/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * Description: shows the main menu and the latest articles with search bar
 * **/

package com.cst2335.kevin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.view.View;
import com.cst2335.R;

import java.util.ArrayList;


public class MainActivityNewYorkTimes extends AppCompatActivity {

    private ArrayAdapter<String> adapterString;
    private ListView listView;
    private ImageButton androidImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_york_times);


        Button searchBtn = findViewById(R.id.nyt_searchButton);
        androidImageButton = findViewById(R.id.nyt_imageBut);

        // clicked on search button to go to new pages with searched results
        searchBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytSearchedActivity.class );
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
                if (position == 0){
                    Intent nextPage = new Intent(MainActivityNewYorkTimes.this, ArticleActivity1.class );
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




    }//onCreate

}//end class

