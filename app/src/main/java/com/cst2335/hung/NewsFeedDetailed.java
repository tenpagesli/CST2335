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

        TextView  tv = (TextView)findViewById(R.id.news_title_detailed);
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        tv.setTypeface(boldTypeface);

        Button saveBtn = (Button)findViewById(R.id.savebtn_hd);
        Button delBtn = (Button)findViewById(R.id.deletebtn_hd);
        Button retBtn = (Button)findViewById(R.id.returnbtn_hd);


        saveBtn.setOnClickListener(c->{
            showToast("Article Saved.");
        });

        delBtn.setOnClickListener(c->{
            alertExample();
        });

        retBtn.setOnClickListener(c->{
        // Snackbar code:
        Snackbar sb = Snackbar.make(retBtn, "Would you like to return? ", Snackbar.LENGTH_LONG)
                .setAction("Yes.", e -> Log.e("Toast", "Clicked return"));
        sb.setAction("Yes.",f -> finish());
        sb.show();
        });
    }
    public void alertExample() {


        View middle = getLayoutInflater().inflate(R.layout.activity_news_feed_popup_delete, null);
        //  Button btn = (Button) middle.findViewById(R.id.extraBtn);
        //EditText et = (EditText) middle.findViewById(R.id.typeHere);

        //  btn.setOnClickListener(clk -> et.setText("You clicked my button!"));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure you want to delete article?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Accept
                        showToast("Article Deleted.");
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // What to do on Cancel
                    }
                }).setView(middle);

        builder.create().show();
    }

public void showToast(String msg){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
}



}




