package com.cst2335.kevin;


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
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NytApiSearch extends AppCompatActivity {

    private TextView titleView, uuidView, articleView; //title, uuid, article view
    private ProgressBar progressBar; //progress bar of Async
    public String aString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_searches);

        //webhose url
        NewsFeedQuery networkThread = new NewsFeedQuery();
        networkThread.execute( "http://torunski.ca/CST2335_XML.xml" );

        progressBar = findViewById(R.id.progressBar_hd);
        progressBar.setVisibility(View.VISIBLE);
        titleView = findViewById(R.id.title_hd);
        uuidView = findViewById(R.id.uuid_hd);

    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class NewsFeedQuery extends AsyncTask<String, Integer, String>
    {

        public String titleAtt; //title value
        public String uuid; //uuid value

        @Override
        protected String doInBackground(String... params) {

            try {
                //get the string url:
                String myUrl = params[0];
                //create the network connection:
                URL url = new URL(myUrl);
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
                        if(tagName.equals("uuid")) {
                            // need to call xpp.next() point to inside of the <uuid> tag;
                            // But if we are looking for an "attribute" of <uuid> tag, then do not need to call xpp.next()"
                            if(xpp.next() == XmlPullParser.TEXT) {
                                uuid = xpp.getText();
                                Log.e("AsyncTask", "Found parameter uuid: " + uuid);
                            }
                            // titleAtt = xpp.getAttributeValue(null, "cheese");
                            publishProgress(15);
                            Thread.sleep(300);
                        }
                        else if(tagName.equals("title")) {
                            // same thing as above
                            if(xpp.next() == XmlPullParser.TEXT) {
                                titleAtt = xpp.getText();
                                Log.e("AsyncTask", "Found parameter titleAtt: " + titleAtt);
                            }
                            // titleAtt = xpp.getAttributeValue(null, "cheese");
                            publishProgress(25);
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

                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin


                URL UVurl = new URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=Tesla&api-key=z0pR0Dz3Ke0loLw2kFTPk3tEPvezSe26");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //now a JSON table:
                JSONObject jObject = new JSONObject(result);
                aString = jObject.getString("status");
                Log.i("UV is:", ""+ aString);

                //END of UV rating

                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            //return type 3, which is String:
            return "Finished task";
        }


        /**
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTask", "update:" + values[0]);

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(15);
            progressBar.setProgress(25);
            progressBar.setProgress(50);
            progressBar.setProgress(75);
            progressBar.setMax(100);
        }

        /**
         * title, uuid, anticle displayed once connection is done
         * @param args
         */
        @Override
        protected void onPostExecute(String args) {
            Log.i("AsyncTask", "onPostExecute" );
            titleView.setText(titleAtt);
            titleView.setText(uuid);
        }


    }


}