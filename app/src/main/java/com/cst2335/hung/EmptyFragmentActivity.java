package com.cst2335.hung;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cst2335.R;


public class EmptyFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_empty_activity);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample


        DetailFragment dFragment = new DetailFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
