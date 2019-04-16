package com.cst2335.hung;



import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.cst2335.R;
import android.content.Intent;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class NewsFeed extends AppCompatActivity{

    String inputPosition; //position for clicks
    NewsFeedDBHelper dbHelper; //db helper
    SQLiteDatabase db; //database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        Button saveButton = findViewById(R.id.save_art_hd);

        //intent from searches
        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition");

        //web view
        WebView webView = findViewById(R.id.webview_hd);
        webView.loadUrl(inputPosition);

        //save button to write to db
        saveButton.setOnClickListener(c -> {
            dbHelper = new NewsFeedDBHelper(this);
            db = dbHelper.getWritableDatabase();
            this.saveArticle(db, inputPosition);
        });
    }

    /**
     * save the article to db
     * @param db
     * @param inputPosition
     */
    private void saveArticle(SQLiteDatabase db, String inputPosition){
        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string word content in the word_content column:
        newRowValues.put(dbHelper.COL_TITLE, inputPosition);
        //insert into the database:
        long newId = db.insert(dbHelper.TABLE_NAME, null, newRowValues);
        String saveMsg = "";
        if(newId>0){
            saveMsg = "Article saved successfully!.";
        }else{
            saveMsg = "Error saving.";
        }
        // show toast bar if saved successful
        Toast.makeText(NewsFeed.this, saveMsg, Toast.LENGTH_LONG).show();
    }

}

