/***
 * Author: Kevin Nghiem
 * Last modified: April 14, 2019
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

    String inputPosition,inputHeadline; //container to pass data in
    NytDataBaseHelper dbInitiate; //initializing the db
    SQLiteDatabase db; //initializing sqlite

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_post);
        Button saveButton = findViewById(R.id.add);

        //from mainactivity saved string it gets passed to this page.
        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition"); //url link
        inputHeadline = previousPage.getStringExtra("inputHeadline"); //article headline


//        System.out.println("url headline " +inputHeadline);
//        System.out.println("url check " +inputPosition);

        //reference from https://stackoverflow.com/questions/22427178/creating-webview-through-android-studio

        WebView webView = findViewById(R.id.wvArticle);
        //url gets placed in here to get populated
        webView.loadUrl(inputPosition);

        //listens for the save button to be clicked then initialize the db and savedword method passes it to the dbase
        saveButton.setOnClickListener(c -> {

            dbInitiate = new NytDataBaseHelper(this);
            db = dbInitiate.getWritableDatabase();
            this.saveWord(db, inputPosition,inputHeadline);
            Intent nextPage = new Intent(ArticleActivity1.this, MainActivityNewYorkTimes.class);

            startActivity(nextPage);

        });
    } //end OnCreate

    /**
     *
     * @param db
     * @param a //should be url
     * @param b //should be headline TO DO but i had trouble getting dbase to work properly only URL can pass through so far
     */
        private void saveWord(SQLiteDatabase db, String a, String b ){
            // get word content
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string word content in the word_content column:
            newRowValues.put(dbInitiate.COL_Article, inputPosition);

//            System.out.println("savedWord here" +inputHeadline);
//            newRowValues.put(dbInitiate.COL_Url, inputPosition);
            //insert into the database:
            long newId = db.insert(dbInitiate.TABLE_NAME, null, newRowValues);
//            System.out.println("kevinnim" +newId);

            // checks if its 1 thing in there or more and it will let me know if the save will work
            String saveMsg = "";
            if(newId >0){
                saveMsg = "Article Saved!.";
            }else{
                saveMsg = "Nothing saved.";
            }

            // show toast bar if saved successful
            Toast.makeText(ArticleActivity1.this, saveMsg, Toast.LENGTH_LONG).show();
        }
}// end class


