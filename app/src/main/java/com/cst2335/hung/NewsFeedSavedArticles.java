package com.cst2335.hung;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cst2335.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsFeedSavedArticles extends AppCompatActivity {
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    NewsFeedDBHelper dbHelper; //db helper
    SQLiteDatabase db; //database
    ArrayList<News> newsArrayList; //news article array list
    SavedWordsAdapter adt; //adapter
    News selectID; //id position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_saved_article);

        //create array
        if (newsArrayList == null) {
            newsArrayList = new ArrayList<>();
        }

        //get database
        dbHelper = new NewsFeedDBHelper(this);
        db = dbHelper.getWritableDatabase();

        // find data and put into db
        this.findAllData(db);

        // list view of saved article
        ListView theList = (ListView) findViewById(R.id.saved_news_list_hd);
        // get fragment
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        //adapter
        adt = new SavedWordsAdapter(newsArrayList);
        theList.setAdapter(adt);

        theList.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, newsArrayList.get(position).getTitle());
            dataToPass.putInt(ITEM_POSITION, position);
            // dataToPass.putLong(ITEM_ID, msgList.get(position).getId());
            dataToPass.putLong(ITEM_ID, id);


            if (isTablet) {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(NewsFeedSavedArticles.this, EmptyFragmentActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition

            }
        });

    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMPTY_ACTIVITY && resultCode == RESULT_OK) {
            long deletedId = data.getLongExtra("deletedId", 0);
            deleteMessageId((int) deletedId);
        }
    }

    /**
     * delete the message from view list by it's id, and update the list
     *
     * @param id
     */

    public void deleteMessageId(int id) {
        Log.i("Delete:", " id=" + id);
        String str = "";
        Cursor c;
        String[] cols = {NewsFeedDBHelper.COL_ID, NewsFeedDBHelper.COL_TITLE};
        c = db.query(false, NewsFeedDBHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            for (int i = 0; i < id; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(NewsFeedDBHelper.COL_ID));
        }
        int x = db.delete(NewsFeedDBHelper.TABLE_NAME, NewsFeedDBHelper.COL_ID + "=?", new String[]{str});
        Log.i("Cursor", "Deleted " + x + " rows");
        newsArrayList.remove(id);
        adt.notifyDataSetChanged();
    }

    /**
     * locate data
     */
    private void findAllData(SQLiteDatabase db) {
        Log.e("FindAllData ", "reached");
        //query all the results from the database:
        String[] columns = {NewsFeedDBHelper.COL_ID, NewsFeedDBHelper.COL_TITLE};
        Cursor results = db.query(false, NewsFeedDBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        // int idColIndex = results.getColumnIndex(NewsFeedDBHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(NewsFeedDBHelper.COL_TITLE);
        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {

            String title = results.getString(contentColumnIndex);
            // String id = results.getString(idColIndex);
            //  String body = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            newsArrayList.add(new News(title, null, null));
            // this.newsArrayList.clear();

        }
    }

    private void findUrl(SQLiteDatabase db) {
        String[] columns = {NewsFeedDBHelper.COL_TITLE};
        Cursor results = db.query(false, NewsFeedDBHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        while (results.moveToNext()) {

        }
    }


    protected class SavedWordsAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //reference to original data
        public SavedWordsAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //array
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
         *   Inflate view of list item for title
         *   @param position: locates the one that will be add to the bottom
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            selectID = (News) getItem(position);
            newView = inflater.inflate(R.layout.activity_news_feed_word_list_item, parent, false);
            //  TextView idView = (TextView) newView.findViewById(R.id.news_id);
            TextView contentView = (TextView) newView.findViewById(R.id.news_title_listitem);

            //Get the string to go in row: position
            // int id = ((News) getItem(position)).getNewsID();
            String content = ((News) getItem(position)).getTitle();
            // String content1 = ((News) getItem(position)).getBody();

            //Set the text of the text view
            // idView.setText("    " + id);
            contentView.setText(content);
            //  contentView.setText(content1);

            return newView;
        }

        /**
         * get position
         *
         * @param position
         * @return
         */
        @Override
        public long getItemId(int position) {
            return (long) position;
        }
    }

}


