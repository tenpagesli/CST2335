/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.R;

import java.util.ArrayList;

public class FlightSavedActivity extends AppCompatActivity {

    ListView savedFlight;
    private ArrayList<Flight> flights;
    private FlightAdapter flightAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_saved);
        savedFlight=findViewById(R.id.saved_flights_qy);
        flights = new ArrayList<Flight>();
        flights.add(new Flight("1024", "locaiton", "speed",
                "altitude", "status", "airportCode"));
        flights.add(new Flight("1025", "locaiton2", "speed2",
                "altitude2", "status2", "airportCode2"));
        //TODO: search for flight
        this.getFlightList();
        // inflate flight summary xml
        flightAdapter = new FlightAdapter(this, 0);
        savedFlight.setAdapter(flightAdapter);
        // update the flight list
        flightAdapter.notifyDataSetChanged();

       // Button deleteBtn = findViewById(R.id.delete);
        // when user clicked on delete button
        savedFlight.setOnItemClickListener((parent,view, position, id )->{
            AlertDialog.Builder builder = new AlertDialog.Builder(FlightSavedActivity.this);
            LayoutInflater inflater2 = FlightSavedActivity.this.getLayoutInflater();
            View dialogview = inflater2.inflate(R.layout.activity_delete, null);
            builder.setView(dialogview);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // delete the selected flight info from database

                    // update the flight list
                    flightAdapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


    }

    protected class FlightAdapter extends ArrayAdapter<Flight> {
        public FlightAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return flights.size();
        }

        public Flight getItem(int position) {
            return flights.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = FlightSavedActivity.this.getLayoutInflater();
            View newView = null;
            Flight flight = getItem(position);
            newView = inflater.inflate(R.layout.activity_flight_summary_qy, null);
            TextView flightNo = newView.findViewById(R.id.flightNo_qy);
            TextView flightStatus = newView.findViewById(R.id.flightStatus_qy);
            flightNo.setText(flight.getFlightNo());
            flightStatus.setText(flight.getStatus());


            return newView;
        }

        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * get flight list from database, and add all flights into flight arrayList
     * */
    private void getFlightList(){
        // TODO:
    }
}
