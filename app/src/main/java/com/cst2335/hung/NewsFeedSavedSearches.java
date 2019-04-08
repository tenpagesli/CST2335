package com.cst2335.hung;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.R;
import com.cst2335.ryan.DetailFragment;
import com.cst2335.ryan.EmptyFragmentActivity;
import com.cst2335.ryan.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsFeedSavedSearches extends AppCompatActivity {
    NewsFeedDBHelper dbInitiate;
    SQLiteDatabase db;
    /** word list */
    ArrayList<News> savedWordList;
    /** Saved Words Adapter */
    SavedWordsAdapter adt;
    /** selected word */
    News selectedWord;

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_saved_searches);

// get savedWordList array list for first time running
        if (savedWordList == null) {
            savedWordList = new ArrayList<>();
        }

        //get a database:
        dbInitiate = new NewsFeedDBHelper(this);
        db = dbInitiate.getWritableDatabase();

        // find all data and put them into message list
        this.findAllData(db);

        // get the "ListView" object
        ListView theList = (ListView)findViewById(R.id.saved_news_list_hd);
        // get fragment
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        // initial the adapter with chatting history list
        adt = new SavedWordsAdapter(savedWordList);
        theList.setAdapter(adt); // the list should show up now

        theList.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, savedWordList.get(position).getTitle());
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
                Intent nextActivity = new Intent(NewsFeedSavedSearches.this, EmptyFragmentActivity.class);
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
        String [] cols = {NewsFeedDBHelper.COL_ID, NewsFeedDBHelper.COL_TITLE};
        c = db.query(false, NewsFeedDBHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            for (int i =0; i<id; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(NewsFeedDBHelper.COL_ID));
        }
        int x = db.delete(NewsFeedDBHelper.TABLE_NAME, NewsFeedDBHelper.COL_ID+"=?", new String[] {str});
        Log.i("ViewContact", "Deleted " + x + " rows");
        savedWordList.remove(id);
        adt.notifyDataSetChanged();
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
        String [] columns = {NewsFeedDBHelper.COL_ID, NewsFeedDBHelper.COL_TITLE};
        Cursor results = db.query(false, NewsFeedDBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        int idColIndex = results.getColumnIndex(NewsFeedDBHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(NewsFeedDBHelper.COL_TITLE);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {

            String content = results.getString(contentColumnIndex);
            int id = results.getInt(idColIndex);
            String content1 = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            savedWordList.add(new News(content, content1,id));
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
            selectedWord = (News)getItem(position);
            newView = inflater.inflate(R.layout.activity_dic_word_list_item, parent, false);
            TextView idView = (TextView) newView.findViewById(R.id.word_id);
            TextView contentView = (TextView) newView.findViewById(R.id.a_word);

            //Get the string to go in row: position
            int id = ((News) getItem(position)).getNewsID();
            String content = ((News) getItem(position)).getTitle();
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


