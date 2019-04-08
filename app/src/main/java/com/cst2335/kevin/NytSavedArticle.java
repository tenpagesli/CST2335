/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * Description: shows the list of searched articles
 * **/

package com.cst2335.kevin;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.cst2335.R;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NytSavedArticle extends AppCompatActivity {

    /** below 2 fields are for database using */
    NytDataBaseHelper dbInitiate;
    SQLiteDatabase db;
    /** below 4 fields are for fragment using */
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;

    /** tool bar */
    Toolbar tBar;
    /** word list */
    ArrayList<Article> savedArticleList;
    /** Saved Words Adapter */
    SavedWordsAdapter adt;
    /** selected Article */
    Article selectArticle;

    /**
     *  this method runs when click on "view saved word list"
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nyt_saved_articles);
        tBar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(tBar);

        // get savedWordList array list for first time running
        if (savedArticleList == null) {
            savedArticleList = new ArrayList<>();
        }

        //get a database:
        dbInitiate = new NytDataBaseHelper(this);
        db = dbInitiate.getWritableDatabase();

        // find all data and put them into message list
        this.findAllData(db);

        // get the "ListView" object
        ListView theList = (ListView)findViewById(R.id.saved_nyt_articles);
        // get fragment
        boolean isTablet = findViewById(R.id.frameLayout) != null; //check if the FrameLayout is loaded
        // initial the adapter with chatting history list
        adt = new SavedWordsAdapter(savedArticleList);
        theList.setAdapter(adt); // the list should show up now

        theList.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, savedArticleList.get(position).getTitle() );
            dataToPass.putInt(ITEM_POSITION, position);
            // dataToPass.putLong(ITEM_ID, msgList.get(position).getId());
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
    }

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


    /**
     * to find all the data, and put them into message list
     */
    private void findAllData(SQLiteDatabase db){
        Log.e("you ", " are looking for all the data");
        //query all the results from the database:
        String [] columns = {NytDataBaseHelper.COL_ID, NytDataBaseHelper.COL_Article};
        Cursor results = db.query(false, NytDataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        int idColIndex = results.getColumnIndex(NytDataBaseHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(NytDataBaseHelper.COL_Article);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            int id = results.getInt(idColIndex);
            String content = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            String content1 = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            savedArticleList.add(new Article(content,content1,id));

        }
    }

    //This class needs 4 functions to work properly:
    protected class SavedWordsAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //Keep a reference to the data:
        public SavedWordsAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //You can give it an array
        public SavedWordsAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }

        public SavedWordsAdapter() {
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
         *  Thsi method will be run list.size() times
         *   @param position: locates the one that will be add to the bottom
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
            int id = ((Article) getItem(position)).getNewsID();
            String content = ((Article) getItem(position)).getTitle();
            //Set the text of the text view
            idView.setText("    " + id);
            contentView.setText(content);
            return newView;
        }

        // get the item id for a specific position in the view list.
        @Override
        public long getItemId(int position) {
            return (long)position;
        }
    }
}