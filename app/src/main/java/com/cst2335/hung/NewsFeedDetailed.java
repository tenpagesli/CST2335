package com.cst2335.hung;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cst2335.R;

public class NewsFeedDetailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed_detailed);

        //set title to bold
        TextView  tv = (TextView)findViewById(R.id.news_title_detailed);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        tv.setTypeface(boldTypeface);





    }





}




