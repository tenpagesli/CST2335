/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * Description: shows the list of searched articles
 * **/

package com.cst2335.kevin;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ProgressBar;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import com.cst2335.R;

public class NytSearchedActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private int mProgressStatus = 0;  //loading time
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_nyt);

        Button saveBtn = findViewById(R.id.nyt_save);
        Button delBtn = findViewById(R.id.nyt_delete);

        mProgressBar = (ProgressBar) findViewById(R.id.nyt_progressBar);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus++;
                    android.os.SystemClock.sleep(5);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgressStatus);
                        }
                    });
                };
            }
             /* this method will call the progress bar
                in a runnable thread
                to do, will add condition when article populates the progress bar will
                return and run by calling the runnable thread.
             */
        }).start();

        /*  when click saved article comfirm saved
            to do when saved it will transfer to the database for record keeping
         */
        saveBtn.setOnClickListener(c -> {
            Toast.makeText(this, "Article Saved", Toast.LENGTH_LONG).show();
        });



        delBtn.setOnClickListener(c -> {
            // confirm with user if really want to delete
            //to do delete from database when article is highlighted and calls delete
            View middle = getLayoutInflater().inflate(R.layout.activity_article_delete, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }

            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    })
                    .setView(middle);
            builder.create().show();
        });

    }
}