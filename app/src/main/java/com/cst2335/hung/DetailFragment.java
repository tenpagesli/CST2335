package com.cst2335.hung;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


import com.cst2335.R;
import com.cst2335.kevin.NytSavedArticle;

public class DetailFragment extends Fragment {

    /** if a tablet */
    private boolean isTablet;
    /** Bundle from previous page */
    private Bundle dataFromActivity;
    /** id from previous page */
    private long id;
    private long db_id;

    NewsFeedDBHelper myDb;
    SQLiteDatabase db;
    WebView webView;

    String url;
    /**
     * set tablet
     * @param tablet
     */
    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     * when the emptyFragment page is launched, run this method
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // get arguments from previous page
        dataFromActivity = getArguments();
        db_id = dataFromActivity.getLong("db_id" );
        id = dataFromActivity.getInt("id" );
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_news_feed_detail_fragment, container, false);

        db_id = dataFromActivity.getLong(NewsFeedSavedArticles.ITEM_ID );
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("Listview ID=" + id);


        //show saved article word
        TextView position = (TextView)result.findViewById(R.id.message);
        idView.setText(dataFromActivity.getString(NewsFeedSavedArticles.ITEM_SELECTED));


        webView = result.findViewById(R.id.wvArticle_hd);
       


        url = dataFromActivity.getString(NewsFeedSavedArticles.ITEM_SELECTED);



       // columns = new String[]{NewsFeedDBHelper.COL_TITLE};
        webView.loadUrl(url);
        //show the id:
        id = dataFromActivity.getLong(NewsFeedSavedArticles.ITEM_ID );



        //show the word content
        TextView contentView = (TextView)result.findViewById(R.id.news_title);
        idView.setText(dataFromActivity.getString(NewsFeedSavedArticles.ITEM_SELECTED));

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener( clk -> {
            if(isTablet) { //both the list and details are on the screen:
                NewsFeedSavedArticles parent = (NewsFeedSavedArticles)getActivity();
                parent.deleteMessageId((int)id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.finish(); //go back
            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                EmptyFragmentActivity parent = (EmptyFragmentActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                long myID = dataFromActivity.getLong(NewsFeedSavedArticles.ITEM_ID);
                backToFragmentExample.putExtra("deletedId", myID);
                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
  //  public News getUrlIndex(){



/*        // 2. build query
        Cursor cursor =
                db.query("NewsFeedDB", // a. table
                        columns, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        // 3. if we got results get the first one*/




/*        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        News book = new News(null,null,null);
        book.setTitle(cursor.getString(1));*/


        //log
/*        Log.d("getBook("+id+")", book.toString());

        // 5. return book
        return book;*/
    //}
    public void viewAll(){
        Cursor cursor = myDb.getAllData();
        if(cursor.getCount() == 0) {
            showMessage("error", "no data found");
        }
            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()){
                buffer.append("title: " + cursor.getString(1));
            }
            showMessage("Data", buffer.toString());
        }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);


    }

}

