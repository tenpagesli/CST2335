package com.cst2335.queeny;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.ryan.MainActivityDictionary;

public class FlightPhoneFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_phone_fragment);

        Bundle bundle = new Bundle();
        bundle.putLong("id",getIntent().getLongExtra("id", 0));
        bundle.putString("flightNo",getIntent().getStringExtra("flightNo"));
        bundle.putString("horizontal",getIntent().getStringExtra("horizontal"));
        bundle.putString("altitude",getIntent().getStringExtra("altitude"));
        bundle.putString("isground",getIntent().getStringExtra("isground"));
        bundle.putString("latitude",getIntent().getStringExtra("latitude"));
        bundle.putString("longitude",getIntent().getStringExtra("longitude"));
        bundle.putString("status",getIntent().getStringExtra("status"));
        bundle.putString("vertical",getIntent().getStringExtra("vertical"));


        // intent.putExtra("isTablet", false);

        FragmentTablet fragmentTablet = new FragmentTablet();
        fragmentTablet.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentTablet).commit();
    }
}
