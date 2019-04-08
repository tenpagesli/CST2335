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

    private TextView copyrightView ;
    private ProgressBar progressBar;
    public String aString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_newyork_api);


        NewsFeedQuery networkThread = new NewsFeedQuery();

       //onClick() {
           networkThread.execute( "http://torunski.ca/CST2335_XML.xml" );
       // }




        progressBar = findViewById(R.id.progressBar_kn);
        progressBar.setVisibility(View.VISIBLE);
        copyrightView = findViewById(R.id.copyright_kn);


    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class NewsFeedQuery extends AsyncTask<String, Integer, String>
    {



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
                aString = jObject.getString("copyright");
                Log.i("copyright is:", ""+ aString);

                //END of UV rating

                publishProgress(15);
                publishProgress(50);

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


            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(15);
            progressBar.setProgress(50);
            progressBar.setMax(100);
        }

        /**
         *
         * @param args
         */
        @Override
        protected void onPostExecute(String args) {
            Log.i("AsyncTask", "onPostExecute" );

            copyrightView.setText(aString);
        }


    }


}
