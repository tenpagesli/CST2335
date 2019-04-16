package com.cst2335.hung;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.cst2335.R;


public class DetailFragment extends Fragment {

    /** if a tablet */
    private boolean isTablet;
    /** Bundle from previous page */
    private Bundle dataFromActivity;
    /** id from previous page */
    private long id;

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
        id = dataFromActivity.getInt("id" ); //set id to unique id
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_news_feed_detail_fragment, container, false);

        //webview
        webView = result.findViewById(R.id.wvArticle_hd);

        //get string from news feed saved articles
        url = dataFromActivity.getString(NewsFeedSavedArticles.ITEM_SELECTED);
        webView.loadUrl(url);

        //store item id into id
        id = dataFromActivity.getLong(NewsFeedSavedArticles.ITEM_ID );

        //delete button
        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener( clk -> {
            if(isTablet) { //both the list and details are on the screen:
                NewsFeedSavedArticles parent = (NewsFeedSavedArticles)getActivity();
                 //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                alertDelete(); //popup delete dialoag box
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

    /**
     * pop up delete dialog box to delete
     */
    public void alertDelete() {

        //pop up custom dialog to ensure user wants to delete article
        View middle = getLayoutInflater().inflate(R.layout.activity_news_feed_popup_delete, null);

        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete article?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        delTablet();
                        getActivity().finish();
                        showToast("Article Deleted.");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                        dialog.dismiss();
                        showToast("Cancelled.");
                    }
                }).setView(middle);

        builder.create().show();
    }

    //toast message
    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * delete id from fragment
     */
    public void delTablet(){
        NewsFeedSavedArticles parent = (NewsFeedSavedArticles)getActivity();
        parent.deleteMessageId((int)id);
    }
    }



