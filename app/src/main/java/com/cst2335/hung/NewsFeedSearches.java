package com.cst2335.hung;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.cst2335.R;



import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsFeedSearches extends AppCompatActivity {
private TextView titleView, uuidView, articleView; //title, uuid, article view
private ProgressBar progressBar; //progress bar of Async

    private ArrayAdapter<String> adapterString;
    ArrayList<News> newsListArticle = new ArrayList<>();
    private ListView newsfeedList;
    private String positionUrl;

    String preUrl = "http://webhose.io/filterWebContent?token=8efc0856-c286-43e6-8d08-0fc945525524&format=xml&sort=relevancy&q=";
    String postUrl = "%20market%20language%3Aenglish";
    NewsFeedDBHelper dbHelper;
    SQLiteDatabase db;
    String URL;
    String searchedArticle;
    //ArrayList<News> newsList = new ArrayList<>();
    public String titleAtt; //title attribute pulled from WEBHOSE.io
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_searches);

        newsfeedList = findViewById(R.id.news_feed_list1);

        // get user input word
        Intent previousPage = getIntent();
        searchedArticle = previousPage.getStringExtra("searchedArticle");
        URL = preUrl + searchedArticle + postUrl;
        //webhose url

        NewsFeedQuery wq = new NewsFeedQuery();
        wq.execute(URL);
        progressBar = findViewById(R.id.progressBar_hd);
        progressBar.setVisibility(View.VISIBLE);
//        titleView = findViewById(R.id.title_hd);
//        uuidView = findViewById(R.id.uuid_hd);

//        Button saveBtn = (Button)findViewById(R.id.savebtn_hd);
//        Button delBtn = (Button)findViewById(R.id.deletebtn_hd);
//        Button retBtn = (Button)findViewById(R.id.returnbtn_hd);

        newsfeedList.setOnItemClickListener(  (parent, view, position, id)->{

        Intent nextPage = new Intent(NewsFeedSearches.this, newFeed1.class);


            System.out.println("hello kevin here" +newsListArticle.get(position).getBody());




            positionUrl = newsListArticle.get(position).getBody();


            System.out.println("hellooweoweowoew");
            System.out.println(positionUrl);
            nextPage.putExtra("inputPosition", positionUrl);



            startActivity(nextPage);
//                startActivity(arrayPass);

        });


        //saving article to db
//        saveBtn.setOnClickListener(c->{
//            showToast("Article Saved.");
//            // save word into database
//            //get a database:
//            dbHelper = new NewsFeedDBHelper(this);
//            db = dbHelper.getWritableDatabase();
//            this.saveWord(db, titleAtt);
//        });

        //deleting article from db
//        delBtn.setOnClickListener(c->{
//            alertDelete();
//        });
//
//        //hitting the return button will show snackbar
//        retBtn.setOnClickListener(c->{
//
//            Snackbar sb = Snackbar.make(retBtn, "Would you like to return? ", Snackbar.LENGTH_LONG)
//                    .setAction("Yes.", e -> Log.e("Toast", "Clicked return"));
//            sb.setAction("Yes.",f -> finish());
//            sb.show();
//        });



        // clicked on view save word button
/*        saveBtn.setOnClickListener(c->{
            // save word into database
            //get a database:
            dbHelper = new MyDatabaseOpenHelper(this);
            db = dbHelper.getWritableDatabase();
            this.saveWord(db, inputWord);
        });*/


    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class NewsFeedQuery extends AsyncTask<String, Integer, String>
    {


        public String uuid; //uuid value
        public String title;

        @Override
        protected String doInBackground(String... params) {

            try {
                //get the string url:
                String myUrl = params[0];
                //create the network connection:
                URL url = new URL(myUrl);

                System.out.println("myUrl");


                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 ); // milliseconds
                conn.setConnectTimeout(15000  ); //milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream inStream = conn.getInputStream();
                //obtain response code, success if 200
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    Log.e("Conection to WEBHOSE", "Successful" );
                } else {
                    Log.e("Conection to WEBHOSE", "FAILED" );
                }

                conn.connect();

                //created a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");


/**             // the commented code below also can be replaced of you code from line 61 to line 86
                String myUrl = params[0];
                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");  //inStream comes from line 46
 */

                //loop over the webhose.io XML:
                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getEventType() == XmlPullParser.START_TAG)
                    {
                        String tagName = xpp.getName();
                        if(tagName.equals("title")) {
                            // need to call xpp.next() point to inside of the <uuid> tag;
                            // But if we are looking for an "attribute" of <uuid> tag, then do not need to call xpp.next()"
                            if(xpp.next() == XmlPullParser.TEXT) {
                                title = xpp.getText();
                                Log.e("AsyncTask", "Found parameter uuid: " + uuid);
                            }
                            // titleAtt = xpp.getAttributeValue(null, "cheese");
                            publishProgress(15);

                        }
                        else if(tagName.equals("url")) {
                            // same thing as above
                            if(xpp.next() == XmlPullParser.TEXT) {
                                titleAtt = xpp.getText();
                                Log.e("AsyncTask", "Found parameter titleAtt: " + titleAtt);
                                News new1 = new News(title, titleAtt, title);
                                newsListArticle.add(new1);

                            }
                            // titleAtt = xpp.getAttributeValue(null, "cheese");
                            publishProgress(25);

                        }



/*                    else if(tagName.equals("dt"))
                    {
                        String definition = xpp.getText();
                        publishProgress(75);
                        Log.e("AsyncTask", "Found parameter definition: "+ definition);
                    }
                    else if(tagName.equals("dt"))
                    {
                        String definition = xpp.getText();
                        definitions.add(definition);
                        publishProgress(75);
                        Log.e("AsyncTask", "Found parameter definition: "+ definition);
                    }*/

                        /**
                         else if(tagName.equals("Temperature")) {
                         xpp.next(); //move to the text between opening and closing tags:
                         String temp = xpp.getText();
                         publishProgress(3); //tell android to call onProgressUpdate with 3 as parameter
                         }
                         **/
                    }
                    xpp.next(); //advance to next XML event
                }

                //Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            return "Finished task";
        }

        /**
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTask", "update:" + values[0]);

//            int size = newsListArticle.size();
//            for (int i = 0; i < size; i++) {
//                NewsAdapter adt = new NewsAdapter(newsListArticle, getApplicationContext());
//                newsfeedList.setAdapter(adt);
//                adt.notifyDataSetChanged();

//            }

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(15);
            progressBar.setProgress(25);
            progressBar.setProgress(50);
            progressBar.setProgress(75);
            progressBar.setProgress(100);
            progressBar.setMax(100);

        }

        /**
         * title, uuid, anticle displayed once connection is done
         * @param args
         */
        @Override
        protected void onPostExecute(String args) {
            Log.i("AsyncTask", "onPostExecute" );
//            titleView.setText(titleAtt);
//            uuidView.setText(uuid);
            NewsAdapter adt = new NewsAdapter(newsListArticle, getApplicationContext());
            newsfeedList.setAdapter(adt);
            adt.notifyDataSetChanged();
        }

    }

    private void saveWord(SQLiteDatabase db, String inputWord){
        // get word content

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string word content in the word_content column:
        newRowValues.put(NewsFeedDBHelper.COL_TITLE, inputWord);
        //insert into the database:
        long newId = db.insert(NewsFeedDBHelper.TABLE_NAME, null, newRowValues);
        String saveResultMessage = "";
        if(newId>0){
            saveResultMessage = "News saved successfully!.";
        }else{
            saveResultMessage = "NOT SAVED!.";
        }
        // show toast bar if saved successful
        Toast.makeText(NewsFeedSearches.this, saveResultMessage, Toast.LENGTH_LONG).show();
    }



    public void alertDelete() {

        //pop up custom dialog to ensure user wants to delete article
        View middle = getLayoutInflater().inflate(R.layout.activity_news_feed_popup_delete, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to delete article?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        showToast("Article Deleted.");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }
    //toast message
    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }



}
