/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cst2335.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityFlightStatusTracker extends AppCompatActivity {
    private ArrayList<Flight> flights;
    private FlightAdapter flightAdapter;
    private  ProgressBar pbar;
    private TextView flightNo_qy,
            flightStatus_qy;

    protected static final String ACTIVITY_NAME = "Flights Search By Airport";
    protected static final String URL_XML = "http://aviation-edge.com/v2/public/flights?key=8a0877-70791c";

    /**
     * When user wants to go to Flight Status Tracker's home page, this method runs
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flight_status_tracker);

        flights = new ArrayList<Flight>();
        flights.add(new Flight("1024", "locaiton", "speed",
                "altitude", "status", "airportCode"));
        flights.add(new Flight("1025", "locaiton2", "speed2",
                "altitude2", "status2", "airportCode2"));
        TextView airportNo = findViewById(R.id.editText2);
        Button search = findViewById(R.id.buttonSeach_qy);
        Button viewSaved = findViewById(R.id.buttonView_qy);
        Button refreshList = findViewById(R.id.buttonRefresh_qy);
        ListView flightList = findViewById(R.id.FlightList_qy);
        // when click on search button
        search.setOnClickListener(c->{
            // get flight list from website
            //TODO: search for flight
            this.getFlightList();
            // inflate flight summary xml
            flightAdapter = new FlightAdapter(this, 0);
            flightList.setAdapter(flightAdapter);

            // show progress bar
            pbar = findViewById(R.id.progress_Bar);
            pbar.setVisibility(View.VISIBLE);
            new FlightQuery().execute(null, null,null);

            // after progress bar shown up, update the flight list
            flightAdapter.notifyDataSetChanged();

            // get the specific flight info which user clicked on, and jump to the details page
            flightList.setOnItemClickListener(  (parent, view, position, id)->{
                //TODO: FORWARD TO FLIGHT DETAILS PAGE

                Intent nextPage = new Intent(MainActivityFlightStatusTracker.this, FlightDetailsActivity.class);
                startActivity(nextPage);
            });
        });

        // when click on view saved flight button
        viewSaved.setOnClickListener(c->{
            // forward to saved flight list page
            Intent nextPage = new Intent(MainActivityFlightStatusTracker.this, FlightSavedActivity.class);

            startActivity(nextPage);
        });
        // when click on refresh button
        refreshList.setOnClickListener(c->{
            // show Snack bar
            Snackbar sb = Snackbar.make(refreshList, "Do you want to refresh?", Snackbar.LENGTH_LONG)
                    .setAction("Yes!", e -> {
                        if(flightAdapter!=null){
                            // update the flight list
                            flightAdapter.notifyDataSetChanged();
                        }
                    });
            sb.show();
            // if agreed, then refresh search list
        });

    }

    /**
     * This is a inner class for the progress bar
     */
    class FlightQuery extends AsyncTask<String, Integer, String> {
        private String flightNo, flightStatus;
        @Override
        protected String doInBackground(String... strings) {
         return null;
        }

        @Override
        protected  void onProgressUpdate(Integer ... value){
            pbar.setVisibility(View.VISIBLE);
            pbar.setProgress(value[0]);
        }

        @Override
        protected  void onPostExecute(String args){
            pbar.setVisibility(View.INVISIBLE);
//            curTemperatureTxt.setText(getString(R.string.currTemp) + currTemp + "℃");
//            minTemperatureTxt.setText(getString(R.string.minTemp) + minTemp + "℃");
//            maxTemperatureTxt.setText(getString(R.string.maxTemp) + maxTemp + "℃");
//            windspeedText.setText(getString(R.string.wind_speed) + windspeed + "m/s");
//            UV_ratingTxt.setText(getString(R.string.UV_rating) + uv_rating + "/rate");

//            weatherImage.setImageBitmap(bmap);
        }


        private boolean isExist(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();

        }
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
            LayoutInflater inflater = MainActivityFlightStatusTracker.this.getLayoutInflater();
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
     * get flight list from web site, and add all flights into flight arrayList
     * */
    private void getFlightList(){
        // TODO:
    }
}
