/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.ryan.MainActivityDictionary;

import java.util.ArrayList;

public class FlightSavedActivity extends AppCompatActivity {
    /** toolbar  */
    Toolbar tBar;
    /**  the flight view list */
    ListView savedFlight;
    /**  the flight list  */
    private ArrayList<Flight> flights;
    /**  the adapter to inflate flights  */
    private FlightAdapter flightAdapter;

    /**
     * When user wants to see the saved flight list, this method runs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_saved);
        // get toolbar
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        savedFlight=findViewById(R.id.saved_flights_qy);
//        flights = new ArrayList<Flight>();
//        flights.add(new Flight("1024", "locaiton", "speed",
//                "altitude", "status", "airportCode"));
//        flights.add(new Flight("1025", "locaiton2", "speed2",
//                "altitude2", "status2", "airportCode2"));
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

    /**
     * This is a inner adapter class for the view list
     */
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
           // flightNo.setText(flight.getFlightNo());
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

    /**
     * inflate the icons for toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_flight_main_qy, menu);
        return true;
    }

    /**
     * when click on the icons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage = null;
        switch(item.getItemId())
        {
            //when click on "dictionary"
            case R.id.go_dic:
                nextPage = new Intent(FlightSavedActivity.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_news_feed:
                nextPage = new Intent(FlightSavedActivity.this, MainActivityNewsFeed.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_new_york:
                nextPage = new Intent(FlightSavedActivity.this, MainActivityNewYorkTimes.class);
                startActivity(nextPage);
                break;
            // when click on "help":
            case R.id.go_help:
                // show help dialog
                View middle = getLayoutInflater().inflate(R.layout.activity_help_dialog, null);
                TextView authorName = (TextView)middle.findViewById(R.id.author_name);
                TextView versionNumber = (TextView)middle.findViewById(R.id.version_number);
                TextView instructions = (TextView)middle.findViewById(R.id.instructions);
                authorName.setText(MyUtil.flightAuther);
                versionNumber.setText(MyUtil.versionNumber);
                instructions.setText(MyUtil.flightInstruction);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                        .setView(middle);

                builder.create().show();
                break;
        }
        return true;
    }
}
