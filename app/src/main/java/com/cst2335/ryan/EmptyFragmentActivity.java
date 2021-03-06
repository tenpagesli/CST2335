package com.cst2335.ryan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cst2335.R;

public class EmptyFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dic_emptiy_fragment);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
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
