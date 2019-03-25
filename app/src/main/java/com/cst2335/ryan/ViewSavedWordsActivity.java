/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: when user clicked on saved word list, this file runs
 * **/
package com.cst2335.ryan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cst2335.R;

public class ViewSavedWordsActivity extends AppCompatActivity {

    /**
     *  this method runs when click on "view saved word list"
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_saved_words);
    }
}
