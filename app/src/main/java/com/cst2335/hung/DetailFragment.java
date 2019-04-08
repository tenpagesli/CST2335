package com.cst2335.hung;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cst2335.R;
import com.cst2335.ryan.EmptyFragmentActivity;

public class DetailFragment extends Fragment {

    /** if a tablet */
    private boolean isTablet;
    /** Bundle from previous page */
    private Bundle dataFromActivity;
    /** id from previous page */
    private long id;

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
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.activity_news_feed_detail_fragment, container, false);

        //show the id:
        id = dataFromActivity.getLong(NewsFeedSavedArticles.ITEM_ID );
        TextView idView = (TextView)result.findViewById(R.id.idText);
        idView.setText("ID is "+ id);

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
}
