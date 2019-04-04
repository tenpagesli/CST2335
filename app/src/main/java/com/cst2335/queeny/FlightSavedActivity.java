/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.ryan.MainActivityDictionary;

import java.util.ArrayList;
import java.util.HashMap;

public class FlightSavedActivity extends AppCompatActivity {
    /** toolbar  */
    Toolbar tBar;
    /**  the flight view list */
    ListView savedFlightListView;
    /**  the flight list  */
    private ArrayList<Flight> flights;
    /**  the adapter to inflate flights  */
    private FlightAdapter flightAdapter;
    /** Database related */
    private SQLiteDatabase db;
    private FlightDataBaseHelper dbOpener;
    /** if it is tablet */
    private boolean isTablet;

    /**
     * When user wants to see the saved flight list, this method runs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_saved);

        isTablet = findViewById(R.id.frameLayout) != null;

        // get toolbar
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        savedFlightListView =findViewById(R.id.saved_flights_qy);
        flights = new ArrayList<>();
        // search saved flights in database
        this.getFlightList();
        // inflate flight summary xml
        flightAdapter = new FlightAdapter(this, 0);
        savedFlightListView.setAdapter(flightAdapter);
        // update the flight list
        flightAdapter.notifyDataSetChanged();

       // Button deleteBtn = findViewById(R.id.delete);
        // when user clicked on delete button, jump to a flight detailed page using iphone, or jump to fragment if using tablet
        savedFlightListView.setOnItemClickListener((parent, view, position, id )->{

            if(isTablet) {
                FragmentTablet fragmentTablet = FragmentTablet.newInstance(true, flights.get(position));

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragmentTablet).commit();
            }else{
                Flight clickedFlight = flights.get(position);
                Intent intent = new Intent(FlightSavedActivity.this, FlightPhoneFragmentActivity.class);
                intent.putExtra("isTablet", false);
                intent.putExtra("id", clickedFlight.getId());
                intent.putExtra("flightNo", clickedFlight.getFlightNo().get("number"));
                intent.putExtra("horizontal", clickedFlight.getSpeed().get("horizontal"));
                intent.putExtra("altitude", clickedFlight.getAltitude());
                intent.putExtra("isground", clickedFlight.getSpeed().get("isGround"));
                intent.putExtra("latitude", clickedFlight.getLocation().get("latitude"));
                intent.putExtra("longitude", clickedFlight.getLocation().get("longitude"));
                intent.putExtra("status", clickedFlight.getStatus());
                intent.putExtra("vertical", clickedFlight.getSpeed().get("vertical"));
                startActivityForResult(intent, 21);
            }


//            AlertDialog.Builder builder = new AlertDialog.Builder(FlightSavedActivity.this);
//            LayoutInflater inflater2 = FlightSavedActivity.this.getLayoutInflater();
//            View dialogview = inflater2.inflate(R.layout.activity_delete, null);
//            builder.setView(dialogview);
//
//            builder.setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
//                    // delete the selected flight info from database
//
//                    // update the flight list
//                    flightAdapter.notifyDataSetChanged();
//            });
//
//            builder.setNegativeButton("No", (DialogInterface dialog, int which)->{
//
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();
        });
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data){
        Log.e("in onActivityResult: ", "start here---------------");
        if(requestCode == 21 && responseCode == Activity.RESULT_OK){
            int id = (int) data.getLongExtra("id",0);
            deleteMessage(id);
        }
    }

    public void deleteMessage(int position) {
        String str="";
        Cursor c;
        String [] cols = {FlightDataBaseHelper.COL_ID};
        c = db.query(false, FlightDataBaseHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            for (int i =0; i<position; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(FlightDataBaseHelper.COL_ID));
        }
        int x = db.delete(FlightDataBaseHelper.TABLE_NAME, "id=?", new String[] {str});
        Log.i("ViewContact", "Deleted " + x + " rows");

//        db.delete(FlightDataBaseHelper.TABLE_NAME, FlightDataBaseHelper.COL_ID + " = ?",
//                new String[]{String.valueOf(flightAdapter.getItemId(position))});
        flights.remove(position);
        flightAdapter.notifyDataSetChanged();
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
            // set iataView and icaoView as invisible
            TextView iataView = newView.findViewById(R.id.iataNumber_qy);
            TextView icaoView = newView.findViewById(R.id.icaoNumber_qy);
            iataView.setVisibility(View.INVISIBLE);
            icaoView.setVisibility(View.INVISIBLE);
            // set values for other views
            TextView flightNo = newView.findViewById(R.id.v_flightNo_qy);
            TextView flightStatus = newView.findViewById(R.id.v_flightStatus_qy);
            flightNo.setText(flight.getFlightNo().get("number"));
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
        //get a database
        dbOpener= new FlightDataBaseHelper(this);
        db = dbOpener.getWritableDatabase();

        //query all the results from database;
        String[] columns = {FlightDataBaseHelper.COL_ID,
                            FlightDataBaseHelper.COL_FLIGHT_NO,
                            FlightDataBaseHelper.COL_LATITUDE,
                            FlightDataBaseHelper.COL_LONGITUDE,
                            FlightDataBaseHelper.COL_HORIZONTAL,
                            FlightDataBaseHelper.COL_ISGROUND,
                            FlightDataBaseHelper.COL_VERTICAL,
                            FlightDataBaseHelper.COL_ALTITUDE,
                            FlightDataBaseHelper.COL_STATUS};
        Cursor results = db.query(false, FlightDataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find tge column indices
        int idColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_ID);
        int flightNoColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_FLIGHT_NO);
        int latitudeColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_LATITUDE);
        int longitudeColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_LONGITUDE);
        int horizontalColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_HORIZONTAL);
        int isgroundColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_ISGROUND);
        int verticalColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_VERTICAL);
        int altitudeColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_ALTITUDE);
        int statusColIndex = results.getColumnIndex(FlightDataBaseHelper.COL_STATUS);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext()){
            String flightNo = results.getString(flightNoColIndex);
            String latitude = results.getString(latitudeColIndex);
            String longitude = results.getString(longitudeColIndex);
            String horizontal = results.getString(horizontalColIndex);
            String isground = results.getString(isgroundColIndex);
            String vertical = results.getString(verticalColIndex);
            String altitude = results.getString(altitudeColIndex);
            String status = results.getString(statusColIndex);
            long id = results.getLong(idColIndex);

            HashMap<String, String> flightNoMap = new HashMap<>();
            flightNoMap.put("number",flightNo);
            HashMap<String, String> locationMap = new HashMap<>();
            locationMap.put("latitude",latitude);
            locationMap.put("longitude",longitude);
            HashMap<String, String> speedNoMap = new HashMap<>();
            speedNoMap.put("horizontal",horizontal);
            speedNoMap.put("isground",isground);
            speedNoMap.put("vertical",vertical);

            flights.add(new Flight(id, flightNoMap, locationMap,
                    null, null,
                    speedNoMap, altitude, status));
        }
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
