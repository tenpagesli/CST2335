/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user wants to see the details of a specific word, this file runs
 * **/
package com.cst2335.ryan;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordsDetailsActivity extends AppCompatActivity {
    /** the url that be used to get the explaination from website */
    String preUrl = "https://www.dictionaryapi.com/api/v1/references/sd3/xml/";
    /** tool bar */
    Toolbar tBar;
    /** to save the word's content */
    String inputWord;
    /** the post part of url **/
    String postUrl = "?key=4556541c-b8ed-4674-9620-b6cba447184f";

    /** the word's view */
    TextView inputWordView;
    /** the parts of speech view */
    TextView posView;
    /** the definition view */
    TextView defView;
    /** single word detail list view */
    ListView preWordsList;
    /** progressBar */
    ProgressBar progressBar;
    // all the results for the searching word
    ArrayList<Word> wordsList = new ArrayList<>();
    /** to save word contents  */
    private Word word;


    /** the final url to search a word */
    String myURL;

    /**
     * this method runs when user click on a specific item of ViewList
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_words_details);

        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        Button saveBtn = findViewById(R.id.save_btn_rl);
        Button deleteBtn = findViewById(R.id.delete_btn_rl);

        preWordsList = findViewById(R.id.dic_search_results_rl);
        progressBar = findViewById(R.id.progress_bar_rl);
        progressBar.setVisibility(View.VISIBLE);  //show the progress bar

        // get user input word
        Intent previousPage = getIntent();
        inputWord = previousPage.getStringExtra("inputWord");
        myURL = preUrl + inputWord + postUrl;
        Log.e("url is: ", myURL);
        // show progress bar
        WordQuery wq = new WordQuery();
        //this starts doInBackground on other thread
        wq.execute(myURL);


        // clicked on view save word button
        saveBtn.setOnClickListener(c->{
            // TODO: save word into database

            // show toast bar if saved successful
            Toast.makeText(WordsDetailsActivity.this, "Word saved successfully!.", Toast.LENGTH_LONG).show();
        });

        // clicked on view delete word button
        deleteBtn.setOnClickListener(c->{
            // confirm with user if really want to delete
            View middle = getLayoutInflater().inflate(R.layout.activity_delete, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(WordsDetailsActivity.this);
            builder.setPositiveButton("Yes", (DialogInterface dialog, int id)-> {
                // TODO: delete word from database

                // show toast bar if deleted successful
                // Toast.makeText(this, "Word deleted successfully!.", Toast.LENGTH_LONG).show();
            })
                    .setNegativeButton("Not Yet", (DialogInterface dialog, int id) ->{
                        // What to do on Cancel

                    })
                    .setView(middle);
            builder.create().show();
        });
    }

    /**
     * when click on save button, then save the word to database
     *
     * @param db
     * @param adt
     * @param buttonClicked
     */
    private void saveWord(SQLiteDatabase db, TextView wordContent, ListAdapter adt, Button buttonClicked){
        // get word content
        String content = wordContent.getText().toString();

        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string word content in the word_content column:
        newRowValues.put(MyDatabaseOpenHelper.COL_CONTENT, content);
        //insert into the database:
        long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

        // create Word Object and add it to the list
        selectedWord = new Word( newId,content , null, null);
        savedWordList.add(selectedWord);
        // update ListView
        ((ViewSavedWordsActivity.SavedWordsAdapter) adt).notifyDataSetChanged();
        //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
        // Snackbar.make(buttonClicked, "Inserted item id:"+newId, Snackbar.LENGTH_LONG).show();
    }

    /**
     * inflate the icons for toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_dictionary_rl, menu);
        return true;
    }

    /**
     * when click on the icons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Snackbar code:
        Snackbar sb = Snackbar.make(tBar, "Do you want to switch to other module?", Snackbar.LENGTH_LONG)
                .setAction("Yes", e -> {
                    Intent nextPage = null;
                    switch(item.getItemId())
                    {
                        //when click on "dictionary"
                        case R.id.go_flight:
                            nextPage = new Intent(WordsDetailsActivity.this, MainActivityFlightStatusTracker.class);
                            startActivity(nextPage);
                            break;
                        //when click on "news feed"
                        case R.id.go_news_feed:
                            nextPage = new Intent(WordsDetailsActivity.this, MainActivityNewsFeed.class);
                            startActivity(nextPage);
                            break;
                        //when click on "new york times"
                        case R.id.go_new_york:
                            nextPage = new Intent(WordsDetailsActivity.this, MainActivityNewYorkTimes.class);
                            startActivity(nextPage);
                            break;

                        // when click on "help":
                        case R.id.go_help:
                            // show help dialog
                            this.showDialog();
                            break;
                    }
                });
        sb.show();
        return true;
    }

    /**
     *  show the help dialog
     */
    private void showDialog(){
        View middle = getLayoutInflater().inflate(R.layout.activity_help_dialog, null);
        TextView authorName = (TextView)middle.findViewById(R.id.author_name);
        TextView versionNumber = (TextView)middle.findViewById(R.id.version_number);
        TextView instructions = (TextView)middle.findViewById(R.id.instructions);
        authorName.setText(MyUtil.dictionaryAuther);
        versionNumber.setText(MyUtil.versionNumber);
        instructions.setText(MyUtil.dictionaryInstruction);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        })
                .setView(middle);

        builder.create().show();
    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class WordQuery extends AsyncTask<String, Integer, String>
    {
        // definition list
        HashMap<String, ArrayList<String>> defiList ;
        // example sentence list
        ArrayList<String> exSenList;
        // all the results for the searching word

        // We don't use namespaces
        String ns = null;

        @Override
        protected String doInBackground(String... params) {

            try {
                // wordsList = new ArrayList<>();
                this.processWordXML(params);
                publishProgress(1);
            }catch (Exception ex){
                Log.e("Crash!!", ex.getMessage() );
            }
            //return type 3, which is String:
            return "Finished task";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.i("AsyncTaskExample", "update:" + values[0]);
            if (values[0] == 1) { // got all the word list from Internet

                // get the adapter to inflate the most recent words list
                WordDetailsAdapter adt = new WordDetailsAdapter(wordsList);
                preWordsList.setAdapter(adt);
                if(!wordsList.isEmpty()){
                    ((WordDetailsAdapter) adt).notifyDataSetChanged();
                }

            }
        }

        @Override
        protected void onPostExecute(String s) {
            //the parameter String s will be "Finished task" from line 27
            // show toast bar if saved successful
            Toast.makeText(WordsDetailsActivity.this, "We found the word for you!", Toast.LENGTH_LONG).show();
            //  messageBox.setText("Finished all tasks");
            // progressBar.setVisibility(View.INVISIBLE);
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
            Log.e("Retrieve data: ", "Finished!!!!");
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
            boolean hadSn = false; // if there is a <sn> before
            boolean hadDt = false; // if there is a <dt> before

            do{
                String tagName = xpp.getName();
                if("entry".equals(tagName)){ // when the tag is <entry>
                    defiList = new HashMap<>(); // when there is a new entry, need a new definition list
                    wordContent = xpp.getAttributeValue(null, "id");
                }else if("fl".equals(tagName)){ // when the tag is <fl>
                    xpp.next();
                    partsOfSpeech = xpp.getText();
                }else if("def".equals(tagName)){ // when the tag is <def>. means there is a new definition
                    // NOTE: because there should be no content directly after this tag
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
                }else if("dt".equals(tagName)){ // when the tag is <dt>
                    // there is a <sn> before. Because one <sn> must match with one <dt>,
                    // no need to add previous definition to list yet because <sn> already added to list
                    if(hadSn){
                        xpp.next();
                        definition += xpp.getText() + " ";
                        hadDt = true;
                    }else{ // no <sn> before.
                        if(hadDt){ // if there is a <dt> before, means this is a new definition.
                            // add previous definition to list
                            if(!"".equals(definition)){
                                defiList.put(definition, exSenList);
                            }
                        }
                        // need a new sentence list for the new definition
                        exSenList = new ArrayList<>();
                        xpp.next();
                        definition += xpp.getText() + " ";
                        hadDt = true;
                    }
                }else if("sx".equals(tagName)){ // when the tag is <sx>
                    // we ignore <sxn>, so add the content of <sx> to definition, it will be the last part of definition
                    String nextTag;
                    do{
                        xpp.next();
                        definition += (xpp.getText() + "; ");
                        xpp.next();
                        nextTag = xpp.getName();
                        if("sxn".equals(nextTag)){
                            xpp.next();
                            xpp.next();
                            xpp.next();
                            xpp.next();
                            nextTag = xpp.getName();
                        }
                        if("sx".equals(nextTag) && xpp.getEventType() == XmlPullParser.END_TAG){
                            xpp.next();
                            nextTag = xpp.getName();
                        }
                    }while("sx".equals(nextTag));
                    defiList.put(definition, exSenList);
                    hadSn = false; // reset
                    hadDt = false; // reset
                    definition = ""; // reset as "" for next definition
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

    /**
     * This class needs 4 functions to work properly:
     * @param <E>
     */
    protected class WordDetailsAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //Keep a reference to the data:
        public WordDetailsAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //You can give it an array
        public WordDetailsAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }

        public WordDetailsAdapter() {
        }

        // return how many items to display
        @Override
        public int getCount() {
            return dataCopy.size();
        }

        // return the contents will show up in each row
        @Override
        public E getItem(int position) {
            return dataCopy.get(position);
        }


        /***
         *  this method set up and add the view that will be added to the bottom of the view list.
         *  This method will be run list.size() times
         *   @param position: locates the one that will be add to the bottom
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            word = (Word)getItem(position);
            newView = inflater.inflate(R.layout.activity_dic_single_word, parent, false);


            // show the input word
            inputWordView = newView.findViewById(R.id.word_content);
            posView = newView.findViewById(R.id.parts_of_speech);
            defView = newView.findViewById(R.id.definition);

            //Get the string to go in row: position
            String wordContent = word.getWord();
            String pos = word.getPartsOfSpeech();
            HashMap<String, ArrayList<String>> defiMaps = word.getDefinitions();
            String defi = "";
            for (Map.Entry<String, ArrayList<String>> pair: defiMaps.entrySet()) {
                if("".equals(defi)){ // the first definition
                    defi = " - "+ pair.getKey();
                }else {
                    defi += "<br/> - "+ pair.getKey();
                }
                // pair.getValue(); // the related example sentences arrayList
            }
            //Set the text of the text view
            inputWordView.setText(wordContent);
            posView.setText(pos);
            defView.setText(Html.fromHtml(defi));

            progressBar.setProgress(100*position/getCount()-1);

            if(position+1==getCount()){
                progressBar.setVisibility(View.INVISIBLE);
            }

            return newView;
        }

        // get the item id for a specific position in the view list.
        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
}
