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
import java.util.HashMap;

public class WordsDetailsActivity extends AppCompatActivity {
    /** the url that be used to get the explaination from website */
    String preUrl = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/";
    /** to save the word's content */
    String inputWord;
     /** the post part of url **/
     String postUrl = "?key=4556541c-b8ed-4674-9620-b6cba447184f";

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
        // definition list
        HashMap<String, ArrayList<String>> defiList = new HashMap<>();
        // example sentence list
        ArrayList<String> exSenList;
        // all the results for the searching word
        ArrayList<Word> wordsList = new ArrayList<>();

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

        /**
         *
         * @param params
         * @throws XmlPullParserException
         * @throws IOException
         */
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

            // Loop for one entry
            while(xpp.getEventType() != XmlPullParser.END_DOCUMENT){
                if(xpp.getEventType() == XmlPullParser.START_TAG){
                    String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                    if(tagName.equals("entry")){
                        xpp = this.getWordFromEntryXML(xpp);
                    }
                }
                xpp.next();
            }
            Log.e("test", "--------------------");
        }

        /**
         * get a Word object by passing in an <Entry> tag
         *
         * eg: "good"'s result as below:
         *
         * good[1]:
         *      adjective:
         *          1 a:of a favorable character or tendency
         *              good news
         *          b: fertile
         *              good land
         * good-hearted:
         *      adjective:
         *          :having a kindly generous disposition
         *      adverb
         *      noun
         *
         * it's structure example of xml file:
         * normally:
         * 1. under <entry> tag: only one <fl> and <def> tag
         *                       <fl> means the "parts of speech"
         *                       <def> means the definition starts. No contents directly after this tag
         * 2. under <def>tag: may be multiple <sn> and <dt> tags.
         *                    the next tag must be <sn> or <dt>, and no other context between <def> and next tag.
         *                   <sn> means the number of one of the definition.
         *                        the next tag must be <dt>. and must have other context between <sn> and <dt>.
         *                   <dt> means the content of each definition
         *                        the next "element" could be context, or <vi> tag, or <sx> tag
         * 3. under <dt> tag: may be multiple <sx> but only one <vi> tags.
         *                    <sx> means one of the content of sub-definitions
         *                    <vi>: between <vi> and </vi> is an example.
         *                         next "element" could be a context or <it>tag.
         * 4-1. under <sx> tag: must be a content (or also has <sxn> tag after content)
         *                     <sxn> tag means a given order for the <sx>. We can ignore the content between <sxn> and </sxn>.
         *                           and consider <sx> or <vi> as the bottom node of the structure.
         * 4-2. under <vi> tag: must be a content or a <it> tag. eg: <vi>it is a <it>good</it> book</it>
         *                    <it>: between <it> and </it> is the searched word its own
         *                    after <it> tag: it is the other parts of the example.
         *
         * <entry id="good[1]">
         *      <fl>adjective</fl>
         *      <def>
         *          <sn>1 a</sn>
         *          <dt>
         *              :of a favorable character or tendency
         *              <vi>
         *                  <it>good</it>
         *                  news
         *              </vi>
         *          </dt>
         *          <sn>b</sn>
         *          <dt>
         *              :
         *              <sx>
         *                  fertile
         *                  <sxn>1</sxn>
         *              </sx>
         *              <vi> // means there is an example
         *                  <it>good</it>
         *                  land
         *              </vi>
         *          </dt>
         *      </def>
         * </entry>
         * <entry id="good-hearted">
         *      <fl>adjective</fl>
         *      <def>
         *          <dt>:having a kindly generous disposition</dt>
         *      </def>
         *      <fl>adverb</fl>
         *      <fl>noun</fl>
         * </entry>
         *
         *
         * @param xpp
         * @return
         */
        private XmlPullParser getWordFromEntryXML(XmlPullParser xpp)throws XmlPullParserException, IOException{

            // the "id" attribute of <entry> tag is the content of the searched word
            String wordContent = "";
            String partsOfSpeech = "";
            String definition = "";
            String exampleSentence = "";
            boolean hadSn = false; // if there is a <sn> before
            boolean hadDt = false; // if there is a <dt> before
            boolean hadSx = false;

            do{
                String tagName = xpp.getName();
                if("entry".equals(tagName)){ // when the tag is <entry>
                    defiList = new HashMap<>(); // when there is a new entry, need a new definition list
                    wordContent = xpp.getAttributeValue(null, "id");
                }else if("fl".equals(tagName)){ // when the tag is <fl>
                    xpp.next();
                    partsOfSpeech = xpp.getText();
                }else if("def".equals(tagName)){ // when the tag is <def>. means there is a new definition
                    // NOTE: because there should be no content directly after this tag,
                    //       however, we still add the definition to the list, just in case for a special situation

                    // add previous definition to definition list
                    if(!"".equals(definition)){
                        defiList.put(definition, exSenList);
                    }
                    // reset definition as "" for next definition
                    definition = "";
                }else if("sn".equals(tagName)){ // when the tag is <sn>. means there is a new definition
                    hadSn = true; // indicate it's a new definition
                    // add previous definition to definition list
                    if(!"".equals(definition)){
                        defiList.put(definition, exSenList);
                    }
                    xpp.next();
                    // reset definition start with the content of <sn> tag for next definition
                    definition = xpp.getText() + " ";
                    // need a new sentence list for the new definition
                    exSenList = new ArrayList<>();
                }else if("dt".equals(tagName)){ // when the tag is <dt>
                    // there is a <sn> before. Because one <sn> must match with one <dt>,
                    // no need to add previous definition to list yet because <sn> already added to list
                    if(hadSn){
                        xpp.next();
                        definition += xpp.getText() + " ";
                    }else{ // no <sn> before.
                        if(hadDt){ // if there is a <dt> before, means it's a new definition.
                            // add previous definition to list
                            if(!"".equals(definition)){
                                defiList.put(definition, exSenList);
                            }
                        }
                        // need a new sentence list for the new definition
                        exSenList = new ArrayList<>();
                        xpp.next();
                        definition += xpp.getText() + " ";
                    }
                }else if("sx".equals(tagName)){ // when the tag is <sx>
                    // we ignore <sxn>, so add the content of <sx> to definition, it will be the last part of definition
                    String nextTag;
                    do{
                        xpp.next();
                        definition += (xpp.getText() + "; ");
                        xpp.next();
                        nextTag = xpp.getName();
                    }while("sx".equals(nextTag));

//                    if("vi".equals(nextTag)){
//                        xpp.next();
//                        if("it".equals(xpp.getName())){
//                            xpp.next();
//                            exampleSentence += (" " + xpp.getText());
//                            xpp.next();
//                            String lastPart = xpp.getText();
//                            if(lastPart!=null && !"".equals(lastPart)){
//                                exampleSentence += (" " + xpp.getText());
//                            }
//                        }else{
//                            exampleSentence = xpp.getText();
//                            if(exampleSentence==null){
//                                exampleSentence = "";
//                            }
//                            xpp.next();
//                        }
//                    }
                    exSenList.add(exampleSentence);
                    defiList.put(definition, exSenList);
                    hadSn = false; // reset
                    hadDt = false; // reset
                    definition = ""; // reset as "" for next definition
                }else if("vi".equals(tagName)){
//                    xpp.next();
//                    exampleSentence = xpp.getText();
//                    if(exampleSentence==null){
//                        exampleSentence = "";
//                    }
//                    xpp.next();
//                    if("it".equals(xpp.getName())){
//                        xpp.next();
//                        exampleSentence += (" " + xpp.getText());
//                        xpp.next();
//                        String lastPart = xpp.getText();
//                        if(lastPart!=null && !"".equals(lastPart)){
//                            exampleSentence += (" " + xpp.getText());
//                        }
//                    }
//                    exSenList.add(exampleSentence);
//                    defiList.put(definition, exSenList);
//                    hadSn = false; // reset
//                    hadDt = false; // reset
//                    definition = ""; // reset as "" for next definition
                }
                xpp.next(); // point to next element
                // if next element is <entry>, then break the loop
                if("entry".equals(xpp.getName()) ){ // while next element is not next <entry> tag yet
                    break;
                }
                if(xpp.getEventType() == XmlPullParser.END_TAG
                        || xpp.getName()==null){
                    xpp.next();
                    continue;
                }
            }while(xpp.getEventType() != XmlPullParser.END_DOCUMENT); // and is start tag
            // add the last word
            Word theWord = new Word(wordContent, defiList, partsOfSpeech);
            wordsList.add(theWord);
            return xpp;
        }
    }




}
