/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * **/

package com.cst2335.kevin;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.cst2335.R;
import android.content.Intent;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class ArticleActivity1 extends AppCompatActivity {

    String inputPosition;
    NytDataBaseHelper dbInitiate;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_post);
        Button saveButton = findViewById(R.id.add);

        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition");

        System.out.println("im cool boy");
        System.out.println(inputPosition);

        WebView webView = findViewById(R.id.wvArticle);
        webView.loadUrl(inputPosition);

        saveButton.setOnClickListener(c -> {
            // save word into database
            //get a database:
            dbInitiate = new NytDataBaseHelper(this);
            db = dbInitiate.getWritableDatabase();
            this.saveWord(db, inputPosition);
        });
    }


        private void saveWord(SQLiteDatabase db, String inputPosition){
            // get word content
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string word content in the word_content column:
            newRowValues.put(dbInitiate.COL_Article, inputPosition);
            //insert into the database:
            long newId = db.insert(dbInitiate.TABLE_NAME, null, newRowValues);
            String saveMsg = "";
            if(newId>0){
                saveMsg = "Article Saved!.";
            }else{
                saveMsg = "Not Completed.";
            }
            // show toast bar if saved successful
            Toast.makeText(ArticleActivity1.this, saveMsg, Toast.LENGTH_LONG).show();
        }









}// end class