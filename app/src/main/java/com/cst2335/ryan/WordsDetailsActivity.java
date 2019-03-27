/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user wants to see the details of a specific word, this file runs
 * **/
package com.cst2335.ryan;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WordsDetailsActivity extends AppCompatActivity {
    /** the url that be used to get the explaination from website */
    String preUrl = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/";
    /** to save the word's content */
    String inputWord;
     /** the post part of url **/
     String postUrl = "?key=4556541c-b8ed-4674-9620-b6cba447184f";

     /** the word object bean*/
     Word word = null;

     /** definition list */
     ArrayList<String> defiList = null;

    /**
     * this method runs when user click on a specific item of ViewList
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_words_details);

        // get user input word
        Intent previousPage = getIntent();
        inputWord = previousPage.getStringExtra("inputWord");
        String myURL = preUrl + inputWord + postUrl;
        Log.e("url is: ", myURL);

        // show the input word
        TextView inputWordView = findViewById(R.id.word);
        inputWordView.setText(inputWord);

        Button saveBtn = findViewById(R.id.save_btn);
        Button deleteBtn = findViewById(R.id.delete_btn);

        // show progress bar
        WordQuery wq = new WordQuery();
        //this starts doInBackground on other thread
        wq.execute(myURL);

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
                Thread.sleep(2000); //pause for 2000 milliseconds to watch the progress bar spin
            }catch (Exception ex){
                Log.e("Crash!!", ex.getMessage() );
            }
            //return type 3, which is String:
            return "Finished task";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            Log.i("AsyncTaskExample", "update:" + values[0]);
//            if (values[0] == 25) {
//                currentTemp.setText("Current temperature is: "+currTemp);
//            }
//            if (values[0] == 50) {
//                minTemp.setText("Minimum temperature is: " + min);
//            }
//            if (values[0] == 75) {
//                maxTemp.setText("Maximum temperature is: " + max);
//                uvRating.setText("UV Rating is: " + uv);
//            }
//            if (values[0] == 100) {
//                bmView.setImageBitmap(bm);
//            }
        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27

            //  messageBox.setText("Finished all tasks");
//            progressBar.setVisibility(View.INVISIBLE);
        }

        private void processWordXML(String... params) throws XmlPullParserException, IOException {
            String currentWord = null; // the current attribute content of id in <entry> tag
            boolean sameDef = false; // if in the same <def> tag. inside this tag, all are definitions
            boolean sameSn = false; // if in the same <sn> tag. inside this tag, it's a number for each defination
          //   boolean sameDt = false; // if in the same <dt> tag. inside this tag, it's ":" or ": with explaination"
            boolean sameSx = false; // if in the same <sx> tag. inside this tag, it's the explaination

            //get the string url:
            String myUrl = params[0];
            String definition = "";

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
                    if(tagName.equals("entry") && xpp.getAttributeValue(null, "id").equals(inputWord))
                    {
                        currentWord = inputWord;
                    }
                    // if this is still inside input word's <entry> tag
                    else if(inputWord.equals(currentWord) && tagName.equals("fl"))
                    {
                        xpp.next();
                        partsOfSpeech = xpp.getText();
                        Log.e("AsyncTask", "Found partsOfSpeech: "+ partsOfSpeech);
                    }
                    else if(inputWord.equals(currentWord) && tagName.equals("def"))
                    {
                        sameDef = true;
                        Log.e("AsyncTask", "Found def tag: ");
                    } // if there is a <sn> under <def> tag
                    else if(inputWord.equals(currentWord)
                            && tagName.equals("sn")
                            && sameDef){
                        Log.e("AsyncTask", "Found sn tag: ");
                        sameSn = true;
                        xpp.next();
                        definition += (xpp.getText()+": ");
                    }
                    // if there is no <sn> under <def> tag (the tag under <def> is <dt>)
                    else if(inputWord.equals(currentWord)
                            && tagName.equals("dt")
                            && sameDef){
                        Log.e("AsyncTask", "Found dt tag: ");
                        xpp.next();
                        definition += (xpp.getText()+" ");
                        definitions.add(definition);
                        // it reaches the bottom of the tree, so set everything as false for next run.
                        sameSn = false;
                        sameDef = false;
                    }
                    // if there is a <sn> under <def> tag
                    else if(inputWord.equals(currentWord)
                            && tagName.equals("dt")
                            && sameDef
                            && sameSn){
                        Log.e("AsyncTask", "Found dt tag: ");
                        xpp.next();
                        definition += (xpp.getText()+" ");
                        definitions.add(definition);
                        // it reaches the bottom of the tree, so set everything as false for next run.
                        sameSn = false;
                        sameDef = false;
                    }
                }
                xpp.next(); //advance to next XML event
            }
        }
    }


}
