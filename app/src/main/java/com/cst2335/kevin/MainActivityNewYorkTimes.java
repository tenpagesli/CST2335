package com.cst2335.kevin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Handler;
import android.view.View;
import com.cst2335.R;


public class MainActivityNewYorkTimes extends AppCompatActivity {


    private ImageButton androidImageButton;
//    private int mProgressStatus = 0;  //loading time
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_york_times);


        Button searchBtn = findViewById(R.id.nyt_searchButton);
        androidImageButton = findViewById(R.id.nyt_imageBut);

        // clicked on search button
        searchBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivityNewYorkTimes.this, NytSearchedActivity.class );
            startActivity(nextPage);
        });

        //click on image to go back to menu
        androidImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar sb = Snackbar.make(androidImageButton, "Welcome To NewYork Times", Snackbar.LENGTH_LONG)
                        .setAction("Go Back to Menu?", e -> finish());
                sb.show();
            }
        });




//        mProgressBar =  findViewById(R.id.nyt_progressBar);
//        mLoadingText = findViewById(R.id.SearchCompleted);
//        btnSimpleSnackbar.setOnClickListener(new View.OnClickListener()
//        {

//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(MainActivityNewYorkTimes.this, "Searched", Toast.LENGTH_LONG).show();
//                ;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while (mProgressStatus < 100){
//                            mProgressStatus++;
//                            android.os.SystemClock.sleep(1);
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mProgressBar.setProgress(mProgressStatus);
//                                }
//                            });
//                        }
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mLoadingText.setVisibility(View.VISIBLE);
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });


    }//onCreate

}//end class

