/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * Description: shows the main menu and the latest articles with search bar
 * **/

package com.cst2335.kevin;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivityNewYorkTimes extends AppCompatActivity {

    private ArrayAdapter<String> adapterString;
    private ListView nyList;
    private ImageButton androidImageButton;
    TextView inputWord;
    SharedPreferences sp;

    public String positionUrl;
    public String nTitle, organization, urlString ;
    Article article;
    ArrayList<Article> articleList = new ArrayList<>();
    protected String preUrl;


    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_york_times);



        nyList = findViewById(R.id.nyt_newsList);
        inputWord = findViewById(R.id.nyt_searchEdit);
        Button searchBtn = findViewById(R.id.nyt_searchButton);
        Button viewSaveArticle = findViewById(R.id.nyt_view_saved);
        androidImageButton = findViewById(R.id.nyt_imageBut);
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar_kn);
        setSupportActionBar(tBar);

        preUrl =" https://api.nytimes.com/svc/search/v2/articlesearch.json?q=Tesla&api-key=z0pR0Dz3Ke0loLw2kFTPk3tEPvezSe26";

        sp = getSharedPreferences("searchedArticle", Context.MODE_PRIVATE);
        // get the value from xml tag of <inputWord>
//        String savedString = sp.getString("inputWord", "");
//        inputWord.setText(savedString);

        // clicked on search button to go to new pages with searched results

        searchBtn.setOnClickListener(c -> {
//            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytApiSearch.class);
//            nextPage.putExtra("inputWord", inputWord.getText().toString());
//            startActivity(nextPage);
            //passes the input string from edit text to the next page clicked


            NewsFeedQuery networkThread = new NewsFeedQuery();
            networkThread.execute(preUrl);

                nyList.setOnItemClickListener(  (parent, view, position, id)->{

                Intent nextPage = new Intent(MainActivityNewYorkTimes.this, ArticleActivity1.class);

//                Intent arrayPass = new Intent(MainActivityNewYorkTimes.this, ArticleActivity1.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("arrayPackage",article);
//                arrayPass.putExtra("hello",bundle);


                System.out.println(articleList.get(position).getArticleID());

                article = articleList.get(position);

                positionUrl = articleList.get(position).getArticleID();
                System.out.println("hellooweoweowoew");
                System.out.println(positionUrl);
                nextPage.putExtra("inputPosition", positionUrl);



                startActivity(nextPage);
//                startActivity(arrayPass);

        });
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


    // a subclass of AsyncTask                  Type1    Type2    Type3
    class NewsFeedQuery extends AsyncTask<String, Integer, String> {
        /**
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            try {
                //get the string url:
                String myUrl = params[0];
                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // milliseconds
                conn.setConnectTimeout(15000); //milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream inStream = conn.getInputStream();

                URL UVurl = new URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=Tesla&api-key=z0pR0Dz3Ke0loLw2kFTPk3tEPvezSe26");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                JSONObject jsonObjectGetNumbers = jObject.optJSONObject("response");
                JSONArray jsonArray = jsonObjectGetNumbers.getJSONArray("docs");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    // Storing each json item in variable

                    nTitle = c.getJSONObject("headline").getString("main");
                    organization = c.getJSONObject("byline").getString("original");
                    urlString = c.getString("web_url");



                    Article article = new Article(nTitle, organization, urlString);
                    articleList.add(article);


//                    ArticleAdapter artAdt = new ArticleAdapter(newsArrayList, getApplicationContext());
//                    ArticleAdapter artAdt1 = new ArticleAdapter(newsArrayList, getApplicationContext());
//                    nyList.setAdapter(artAdt);
//                    nyList.setAdapter(artAdt1);

                }
//                articleList.add(article);
                //END of UV rating

                publishProgress(15);
                publishProgress(50);

            } catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }

            //return type 3, which is String:
            return "Finished task";
        }


        /**
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

            int size = articleList.size();
            for (int i = 0; i < size; i++) {
                ArticleAdapter adt = new ArticleAdapter(articleList, getApplicationContext());
                nyList.setAdapter(adt);

            }

//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(15);
//            progressBar.setProgress(50);
//            progressBar.setMax(100);
        }
    }

    }//end class

