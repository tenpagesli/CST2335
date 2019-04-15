/***
 * Author: Kevin Nghiem
 * Last modified: April 14, 2019
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
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    //saves the pre and post http for the search because the search input is between them both
    protected String preUrl = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=";
    protected String postUrl = "&api-key=z0pR0Dz3Ke0loLw2kFTPk3tEPvezSe26";
    private ListView nyList; // initialize list view to be called
    private ImageButton androidImageButton; // initialize Image to be called as a button
    private ProgressBar progressBar; // initialize list view to be called
    TextView inputWord; // initialize input to be called
    SharedPreferences sp; //initlize sp to be called
    private String Url, search;
    public String positionUrl,positionHeadline; // container for passing to next activity
    public String nTitle, organization, urlString ; // container for the api to pass it into the array to be stored
    Article article; //calling the array
    ArrayList<Article> articleList = new ArrayList<>(); //calling the arraylist

    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_york_times);

        //calling all xml tags and placing with the container name
        progressBar = findViewById(R.id.nyt_progressBar);
        nyList = findViewById(R.id.nyt_newsList);
        inputWord = findViewById(R.id.nyt_searchEdit);
        Button searchBtn = findViewById(R.id.nyt_searchButton);
        Button viewSaveArticle = findViewById(R.id.nyt_view_saved);
        androidImageButton = findViewById(R.id.nyt_imageBut);
        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar_kn);
        setSupportActionBar(tBar);

        //save the inputtext from search and keep it there when you re open it.
        sp = getSharedPreferences("searchedArticle", Context.MODE_PRIVATE);
//      get the value from xml tag of <inputWord>
        String savedString = sp.getString("inputWord", "");
        inputWord.setText(savedString);


//        System.out.println("kevin");
//        System.out.println(inputWord.getText().toString());
//        System.out.println("nim");

        // listens for the search button to be clicked then starts the database  thread
        searchBtn.setOnClickListener(c -> {
//            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytApiSearch.class);
//            nextPage.putExtra("inputWord", inputWord.getText().toString());
//            startActivity(nextPage);
            CharSequence a = inputWord.getText();
            if(inputWord.getText()==null || "".equals(inputWord.getText().toString())){

                return;
            }
            NewsFeedQuery networkThread = new NewsFeedQuery();
            networkThread.execute(preUrl);

            searchBtn.onEditorAction(EditorInfo.IME_ACTION_DONE); //https://www.youtube.com/watch?v=V54largrb7E
            progressBar.setProgress(0);  //resets the progress bar to 0 after another search is done
            articleList.clear();  //resets the array so it can repopulate on the same page
//            System.out.println(inputWord.getText().toString());

            search = inputWord.getText().toString();
//            System.out.println("search test" +search);
            Url = preUrl + search + postUrl;
//            System.out.println("myurl" +Url);
//            inputWord.setText("");


            nyList.setOnItemClickListener(  (parent, view, position, id)->{

            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, ArticleActivity1.class);

//                System.out.println(articleList.get(position).getArticleID());
//                System.out.println(articleList.get(position).getTitle());

             article = articleList.get(position);
             positionUrl = articleList.get(position).getArticleID();

             positionHeadline = articleList.get(position).getTitle();
//                System.out.println("hellooweoweowoew");
//                System.out.println(positionUrl);
             nextPage.putExtra("inputPosition", positionUrl);
             nextPage.putExtra("inputHeadline", positionHeadline );

             startActivity(nextPage);
           }); // end nyList
        }); // end searchbutton

        //listen for the button for saved arrticle page
        viewSaveArticle.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytSavedArticle.class );
            startActivity(nextPage);
        });

        //click on image to go back to main menu with snackbar
        androidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sb = Snackbar.make(androidImageButton, "Welcome To NewYork Times", Snackbar.LENGTH_LONG)
                        .setAction("Go Back To Main Menu?", e -> finish());
                sb.show();
            }
        });
    }// end on create

    /**
     *
     * @param item
     * @return
     */
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
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_new_york, menu);
        return true;
    }
    //when case go_help gets called it will inflate the msg with the string on this method.
    private void showDialog () {
        //pop up custom dialog to show Activity Version, Author, and how to use news feed
        View middle = getLayoutInflater().inflate(R.layout.activity_main_news_feed_help_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Activity Version: 1.0\n" +
                "Author: Kevin\n" +
                "How to use: Enter search term of the article. Click on article for further details.")

                .setNegativeButton("OK.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);
        builder.create().show();
    }


    //taken from the weather lab 7 and reconfigure for nyt api
    // a subclass of AsyncTask                  Type1    Type2    Type3
    class NewsFeedQuery extends AsyncTask<String, Integer, String> {
        /**
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(Url); //url gets pass when search button is clicked
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // milliseconds
                conn.setConnectTimeout(15000); //milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream inStream = conn.getInputStream();

                URL UVurl = new URL(Url);
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


                    // after the json object/array finish looping all the article it stores it all in article array then adds it to article list.
                    Article article = new Article(nTitle, organization, urlString);
                    articleList.add(article);


//                    ArticleAdapter artAdt = new ArticleAdapter(newsArrayList, getApplicationContext());
//                    ArticleAdapter artAdt1 = new ArticleAdapter(newsArrayList, getApplicationContext());
//                    nyList.setAdapter(artAdt);
//                    nyList.setAdapter(artAdt1);

                }
                publishProgress(15);
                publishProgress(50);

            } catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }
            return "Finished task";
        }

        /**
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

//            int size = articleList.size();
//            for (int i = 0; i < size; i++) {
            // places the arraylist of arrticle into the adapter the populating the listview by setting it to the adapter.
            ArticleAdapter adt = new ArticleAdapter(articleList, getApplicationContext());
            nyList.setAdapter(adt);
         //the progress bar will go to 100% after the the search is listed to the listview.
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(15);
            progressBar.setProgress(50);
            progressBar.setProgress(100);
            progressBar.setMax(100);
        }
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

    }//end class

