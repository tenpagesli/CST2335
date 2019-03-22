package com.cst2335.ryan;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cst2335.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WordsDetailsActivity extends AppCompatActivity {
    String preUrl = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/";
    String aWord;
    String postUrl = "?key=4556541c-b8ed-4674-9620-b6cba447184f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_details);
        Button saveBtn = findViewById(R.id.save_btn);
        Button deleteBtn = findViewById(R.id.delete_btn);

        // show progress bar
        WordQuery wq = new WordQuery();
        //this starts doInBackground on other thread
        wq.execute(preUrl + "happy" + postUrl);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar

        // clicked on view save word button
        saveBtn.setOnClickListener(c->{
            // TODO: save word into database

            // show toast bar if saved successful
            Toast.makeText(this, "Word saved successfully!.", Toast.LENGTH_LONG).show();
        });

        // clicked on view delete word button
        deleteBtn.setOnClickListener(c->{
            // confirm with user if really want to delete
            View middle = getLayoutInflater().inflate(R.layout.activity_delete, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // TODO: delete word from database

                    // show toast bar if deleted successful
                    // Toast.makeText(this, "Word deleted successfully!.", Toast.LENGTH_LONG).show();
                }
            })
                    .setNegativeButton("Not Yet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // What to do on Cancel
                        }
                    })
                    .setView(middle);
            builder.create().show();
        });
    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class WordQuery extends AsyncTask<String, Integer, String>
    {
        String word;
        String partsOfSpeech;
        ArrayList<String> definitions;
        String exampleSentence;

        // We don't use namespaces
        String ns = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                this.processWordXML(params);
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
        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27

            //  messageBox.setText("Finished all tasks");
            //  progressBar.setVisibility(View.INVISIBLE);
        }

        private void processWordXML(String... params) throws XmlPullParserException, IOException {
            //get the string url:
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

            //loop over the weather XML:
            while(xpp.getEventType() != XmlPullParser.END_DOCUMENT)
            {
                if(xpp.getEventType() == XmlPullParser.START_TAG)
                {
                    String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                    if(tagName.equals("fl"))
                    {
                      //  word = ;
                        publishProgress(25); //tell android to call onProgressUpdate with 1 as parameter
                        partsOfSpeech = xpp.getText();
                        Log.e("AsyncTask", "Found parameter parts Of Speech: "+ partsOfSpeech);
                        publishProgress(50); //tell android to call onProgressUpdate with 1 as parameter

                    }
                    else if(tagName.equals("dt"))
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
                    }

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
        }
    }


}
