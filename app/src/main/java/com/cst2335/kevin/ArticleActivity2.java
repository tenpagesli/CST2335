package com.cst2335.kevin;

/***
 * Author: Kevin Nghiem
 * Last modified: April 14, 2019
 * Description: not used anymore use for backup.
 * **/


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.cst2335.R;
import android.content.Intent;
import android.widget.Button;


public class ArticleActivity2 extends AppCompatActivity {

    String inputPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_post);
        Button back = findViewById(R.id.Back);

        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition");


        System.out.println(inputPosition);

        WebView webView = findViewById(R.id.wvArticle);
        webView.loadUrl(inputPosition);


    }


    }









