package com.cst2335.hung;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import com.cst2335.R;
import com.cst2335.ryan.WordsDetailsActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsFeedSearches extends AppCompatActivity {
private TextView titleView;
private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_searches);


        // show progress bar
        NewsFeedQuery wq = new NewsFeedQuery();
        //this starts doInBackground on other thread
        wq.execute("http://webhose.io/filterWebContent?token=86940a5c-b094-4465-942e-81ce096fe5c9&format=xml&sort=crawled&q=samsung");

        progressBar = findViewById(R.id.progressBar_hd);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar
        titleView = findViewById(R.id.title_hd);

    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class NewsFeedQuery extends AsyncTask<String, Integer, String>
    {
        String word;
        String partsOfSpeech;
        ArrayList<String> definitions;
        String exampleSentence;
        public String titleAtt;

        // We don't use namespaces
        String ns = null;

        @Override
        protected String doInBackground(String... params) {

            try {

                //get the string url:
                String myUrl = params[0];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream inStream = conn.getInputStream();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    Log.e("Conection to WEBHOSE", "Successful" );
                } else {
                    Log.e("Conection to WEBHOSE", "FAILED" );
                }

                conn.connect();

                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);

                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput( inStream  , "UTF-8");  //inStream comes from line 46

                //loop over the weather XML:
                while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
                {
                    if(xpp.getEventType() == XmlPullParser.START_TAG)
                    {
                        String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                        if(tagName.equals("title")) {

                                //  word = ;
                                publishProgress(25); //tell android to call onProgressUpdate with 1 as parameter
                                Thread.sleep(300);
                                //  partsOfSpeech = xpp.getText();
                                //  titleAtt = xpp.getText();
                                titleAtt = xpp.getAttributeValue(null, "cheese");
                                Log.e("AsyncTask", "Found parameter : " + titleAtt);
                                publishProgress(50); //tell android to call onProgressUpdate with 1 as parameter
                                Thread.sleep(300);

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
                // load over UC rating xml
                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin
            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }
            //return type 3, which is String:
            return "Finished task";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTaskExample", "update:" + values[0]);
            //  messageBox.setText("At step:" + values[0]);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(25);
            progressBar.setProgress(50);
            progressBar.setProgress(75);
            progressBar.setProgress(75);
            progressBar.setMax(100);
        }

        @Override
        protected void onPostExecute(String args) {
            //the parameter String s will be "Finished task" from line 27

            //  messageBox.setText("Finished all tasks");
            //  progressBar.setVisibility(View.INVISIBLE);
            titleView.setText("TITLE:" + titleAtt);
        }


    }


}
