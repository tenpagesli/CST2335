/***
 * Author: Kevin Nghiem
 * Last modified: april 14, 2019
 * Description: Show the saved article
 * **/

package com.cst2335.kevin;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.queeny.MainActivityFlightStatusTracker;
import com.cst2335.ryan.MainActivityDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NytSavedArticle extends AppCompatActivity {

    //database reference for proper data to be passed
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;

    NytDataBaseHelper dbInitiate; //datahelper gets called
    SQLiteDatabase db; //sqlite help determine what goes where in the database
    Toolbar tBar;   //toolbar
    ArrayList<Article> savedArticleList; //article list
    SavedWordsAdapter adt;     //saved article word
    Article selectArticle;     //selected article

    /**
     *  this method runs when click on "view saved word list"
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyt_saved_articles);
        tBar = findViewById(R.id.toolbar);

        setSupportActionBar(tBar);

        // get savedWordList array list for first time running
        if (savedArticleList == null) {
            savedArticleList = new ArrayList<>();
        }

        //get the database ready
        dbInitiate = new NytDataBaseHelper(this);
        db = dbInitiate.getWritableDatabase();

        //this initalize the database then goes into the findall method to start populated the array List to be used and parse out
        // for me to use to add to the webcontent
        this.findAllData(db);

        // get the "ListView" object
        // get fragment
        // initial the adapter with article history list
        ListView theList = findViewById(R.id.saved_article_list);
        boolean isTablet = findViewById(R.id.frameLayout) != null; //check if the FrameLayout is loaded
        adt = new SavedWordsAdapter(savedArticleList);

        // this will populate the listview by passing adt data inside
        theList.setAdapter(adt);

//        System.out.println("your here" + savedArticleList.get(0).getArticleID());
//        System.out.println("your here" + savedArticleList.get(0).getOrganization());
//        System.out.println("your here" + savedArticleList.get(0).getTitle());


            //listen for the click item then passes the data to the fragment pages and also checks if its a cell phone or a tablet
            theList.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, savedArticleList.get(position).getTitle() );
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);


            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NytSavedArticle.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });
    } // end on create

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMPTY_ACTIVITY && resultCode == RESULT_OK) {
            // for phone, check if deleted a message
            long deletedId = data.getLongExtra("deletedId", 0);
            // delete from database
            // dbOpener.deleteRow(db, deletedId);
            deleteMessageId((int)deletedId);
        }
    }

    /**
     *  delete the message from view list by it's id, and update the list
     * @param id
     */

    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        String str="";
        Cursor c;
        String [] cols = {NytDataBaseHelper.COL_ID, NytDataBaseHelper.COL_Article};
        c = db.query(false, NytDataBaseHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            for (int i =0; i<id; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(NytDataBaseHelper.COL_ID));
        }
        int x = db.delete(NytDataBaseHelper.TABLE_NAME, NytDataBaseHelper.COL_ID+"=?", new String[] {str});
        Log.i("ViewContact", "Deleted " + x + " rows");
        savedArticleList.remove(id);
        adt.notifyDataSetChanged();
    }
    /**
     * inflate the icons for toolbar
     * @param
     * @return
     */
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        Intent nextPage = null;
        switch (item.getItemId()) {
            //when click on "dictionary"
            case R.id.go_flight:
                nextPage = new Intent(NytSavedArticle.this, MainActivityFlightStatusTracker.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_dic:
                nextPage = new Intent(NytSavedArticle.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_news_feed:
                nextPage = new Intent(NytSavedArticle.this, MainActivityNewsFeed.class);
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
        inflater.inflate(R.menu.toolbar_menu_newsfeed_hd, menu);
        return true;
    }
    //when case go_help gets called it will inflate the msg with the string on this method.
    private void showDialog () {
        //pop up custom dialog to show Activity Version, Author, and how to use news feed
        View middle = getLayoutInflater().inflate(R.layout.activity_main_news_feed_help_popup, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Activity Version: 1.0\n" +
                "Author: Kevin\n" +
                "This is the Bookmark page you could open your saved Bookmark or Delete them.")

                .setNegativeButton("OK.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);
        builder.create().show();
    }
    /**
     *
     * @param db
     * to find all the data in the database and passes it into a arraylist
     */
    private void findAllData(SQLiteDatabase db){
        Log.e("you ", " are looking for all the data");
        //query all the results from the database:
        String [] columns = {NytDataBaseHelper.COL_ID, NytDataBaseHelper.COL_Article};
        Cursor results = db.query(false, NytDataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        int idColIndex = results.getColumnIndex(NytDataBaseHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(NytDataBaseHelper.COL_Article);
//        int urlColumnIndex = results.getColumnIndex(NytDataBaseHelper.COL_Url);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String id = results.getString(idColIndex);
            String content = results.getString(contentColumnIndex);
            String content1 = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            //TO DO fix naming convention for easier to read
            savedArticleList.add(new Article(content,content1,id));

        }
    }

    //this will populate the adapter with the arraylist to them used for the lisview and poplulates the favorites
    protected class SavedWordsAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;
        //Keep a reference to the data:
        public SavedWordsAdapter(List<E> originalData) {
            dataCopy = originalData;
        }
        public SavedWordsAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }
        public SavedWordsAdapter() {
        }
        // return  the size
        @Override
        public int getCount() {
            return dataCopy.size();
        }
        // return the contents that shows up
        @Override
        public E getItem(int position) {
            return dataCopy.get(position);
        }

        /***
         *  this method will populate the data into a text view then add it into a listview as 1 list per arraylist object
         *   @param position: locate the pos and add to the bott
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            selectArticle = (Article)getItem(position);
            newView = inflater.inflate(R.layout.activity_nyt_article_list, parent, false);
            TextView idView = (TextView) newView.findViewById(R.id.article_id);
            TextView contentView = (TextView) newView.findViewById(R.id.a_article);

            //Get the string to go in row: position
            String id = ((Article) getItem(position)).getTitle();
//            String content = ((Article) getItem(position)).getTitle();
            //Set the text of the text view
            idView.setText("    " + id);
//            contentView.setText(content);
            return newView;
        }
        // gets the Item id to add to the position
        @Override
        public long getItemId(int position) {
            return (long)position;
        }
    }
}