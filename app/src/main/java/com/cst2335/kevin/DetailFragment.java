package com.cst2335.kevin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.cst2335.R;

public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long db_id;
    private int id;
    private String webUrl;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //data is passed from NYTsaved article
        dataFromActivity = getArguments();
        db_id = dataFromActivity.getLong("db_id" );
        id = dataFromActivity.getInt("id" );
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_nyt_fragment, container, false);


        //show the article ID
        db_id = dataFromActivity.getLong(NytSavedArticle.ITEM_ID );
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("Listview ID=" + id);


        //show saved article word/url
        TextView position = (TextView)result.findViewById(R.id.message);
        idView.setText(dataFromActivity.getString(NytSavedArticle.ITEM_SELECTED));

        //testing whats inside the selected string
        System.out.println("check here");
        System.out.println(dataFromActivity.getString(NytSavedArticle.ITEM_SELECTED));
        System.out.println("ends here");

        //String added to the webURL container
        webUrl = dataFromActivity.getString(NytSavedArticle.ITEM_SELECTED);

        //webURL gets placed into the webView to show the Article and load it
        WebView webView = result.findViewById(R.id.wvArticle1);
        webView.loadUrl(webUrl);


        //TO DO fix naming convention
        // this listens for the click button and goes back to the page before this one
        Button goBack = result.findViewById(R.id.open_activity2);
        goBack.setOnClickListener( clk -> {

            if(isTablet) {
            NytSavedArticle parent = (NytSavedArticle)getActivity();
            parent.finish(); //go back
            }
            else {//phone
                EmptyActivity parent = (EmptyActivity) getActivity();
                parent.finish(); //go back
            }
        });

        // get the delete button, and add a click listener and deletes the bookmark item from the database
        Button deleteButton = result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

                //checks if it was clicked on a tablet
            if(isTablet) { //both the list and details are on the screen:
                NytSavedArticle parent = (NytSavedArticle)getActivity();
                parent.deleteMessageId((int)db_id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.finish(); //go back
            }
            //checks if it was clicked on a phone
            else {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("db_id", dataFromActivity.getLong("db_id"));
                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;


}


}