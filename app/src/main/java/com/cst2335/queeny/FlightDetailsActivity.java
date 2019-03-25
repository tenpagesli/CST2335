/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.cst2335.R;

public class FlightDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);
        Button saveFlight = findViewById(R.id.save_btn);
        // when click on save button
        saveFlight.setOnClickListener(c->{
            Toast.makeText(FlightDetailsActivity.this, "The flight has already saved", Toast.LENGTH_LONG).show();
        });
    }
}
