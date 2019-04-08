/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user clicked on saved word list, this file runs
 * **/
package com.cst2335.ryan;

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

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.queeny.MainActivityFlightStatusTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewSavedWordsActivity extends AppCompatActivity {
    /** below 2 fields are for database using */
    MyDatabaseOpenHelper dbOpener;
    SQLiteDatabase db;
    /** below 4 fields are for fragment using */
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;

    /** tool bar */
    Toolbar tBar;
    /** word list */
    ArrayList<Word> savedWordList;
    /** Saved Words Adapter */
    SavedWordsAdapter adt;
    /** selected word */
    Word selectedWord;

    /**
     *  this method runs when click on "view saved word list"
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_view_saved_words);

        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        // get savedWordList array list for first time running
        if (savedWordList == null) {
            savedWordList = new ArrayList<>();
        }

        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        // find all data and put them into message list
        this.findAllData(db);

        // get the "ListView" object
        ListView theList = (ListView)findViewById(R.id.saved_words_list);
        // get fragment
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        // initial the adapter with chatting history list
        adt = new SavedWordsAdapter(savedWordList);
        theList.setAdapter(adt); // the list should show up now

        theList.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, savedWordList.get(position).toString() );
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
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ViewSavedWordsActivity.this, EmptyFragmentActivity.class);
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
        String [] cols = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_CONTENT};
        c = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            for (int i =0; i<id; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(MyDatabaseOpenHelper.COL_ID));
        }
        int x = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID+"=?", new String[] {str});
        Log.i("ViewContact", "Deleted " + x + " rows");
        savedWordList.remove(id);
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
                            nextPage = new Intent(ViewSavedWordsActivity.this, MainActivityFlightStatusTracker.class);
                            startActivity(nextPage);
                            break;
                        //when click on "news feed"
                        case R.id.go_news_feed:
                            nextPage = new Intent(ViewSavedWordsActivity.this, MainActivityNewsFeed.class);
                            startActivity(nextPage);
                            break;
                        //when click on "new york times"
                        case R.id.go_new_york:
                            nextPage = new Intent(ViewSavedWordsActivity.this, MainActivityNewYorkTimes.class);
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

    /**
     * to find all the data, and put them into message list
     */
    private void findAllData(SQLiteDatabase db){
        Log.e("you ", " are looking for all the data");
        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_CONTENT};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_CONTENT);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String content = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            savedWordList.add(new Word(id, content, null, null));
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
            selectedWord = (Word)getItem(position);
            newView = inflater.inflate(R.layout.activity_dic_word_list_item, parent, false);
            TextView idView = (TextView) newView.findViewById(R.id.word_id);
            TextView contentView = (TextView) newView.findViewById(R.id.a_word);

            //Get the string to go in row: position
            long id = ((Word) getItem(position)).getId();
            String content = getItem(position).toString();
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
