/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cst2335.MainActivity;
import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.ryan.MainActivityDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivityFlightStatusTracker extends AppCompatActivity {
    /** toolbar  */
    Toolbar tBar;
    /**  flight list */
    private ArrayList<Flight> flights;
    /** the adapter to inflate the flights */
    private FlightAdapter flightAdapter;
    /**  progress bar  */
    private  ProgressBar pbar;
    /**  the flight number and flight status */
    private TextView flightNo_qy, flightStatus_qy;
    /**  activity name */
    protected static final String ACTIVITY_NAME = "Flights Search By Airport";
    /**  flight url */
    protected static final String myUrl = "http://aviation-edge.com/v2/public/flights?key=8a0877-70791c";
    /** airport code test view */
    TextView airportCode;
    Button search;
    Button viewSaved;
    Button refreshList;
    ListView flightList;

    /**
     * When user wants to go to Flight Status Tracker's home page, this method runs
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flight_status_tracker);
        // get toolbar
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        flights = new ArrayList<Flight>();
//        flights.add(new Flight("1024", "locaiton", "speed",
//                "altitude", "status", "airportCode"));
//        flights.add(new Flight("1025", "locaiton2", "speed2",
//                "altitude2", "status2", "airportCode2"));
        airportCode = findViewById(R.id.editText2);
        search = findViewById(R.id.buttonSeach_qy);
        viewSaved = findViewById(R.id.buttonView_qy);
        refreshList = findViewById(R.id.buttonRefresh_qy);
        flightList = findViewById(R.id.FlightList_qy);
        // when click on search button
        search.setOnClickListener(c->{
            // get flight list from website
            FlightQuery fq = new FlightQuery();
            fq.execute(myUrl); // will run doInBackground()

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
            //get the string url:
            String myUrl = strings[0];
            try {
                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                JSONArray array;
                try {
                    array = new JSONArray(sb.toString());
                    for(int i=0;i<array.length();i++){
                        JSONObject obj = array.getJSONObject(i);
                        // get your data from jsonObject
                        JSONArray departures = obj.getJSONArray("departure");
                        if(departures.getString(0).equals(airportCode.getText().toString())
                           || departures.getString(1).equals(airportCode.getText().toString())){

//                            HashMap<String, String> flightNo = new HashMap<>();
//                            flightNo.put("iataNumber",obj.getJSONObject("flight").getJSONObject("iataNumber").toString());
//                            flightNo.put("icaoNumber",obj.getJSONObject("flight").getJSONObject("icaoNumber").toString());
//                            flightNo.put("number",obj.getJSONObject("flight").getJSONObject("number").toString());
                            HashMap<String, String>  departure = new HashMap<>();
                            HashMap<String, String>  arrival = new HashMap<>();
                            HashMap<String, String>  speed = new HashMap<>();
                            String  altitude = null;
                            String status = null;


//
//                            Flight flight = new Flight(obj.getJSONObject("flight").getJSONObject("number"),
//                                    obj.getJSONObject("departure").getJSONObject("iataCode")
//                                    , String speed, String altitude, String status, String airportCode);
//
//                            new Flight(new HashMap<String>, HashMap<String, String> departure, HashMap<String, String> arrival,
//                                    HashMap<String, String> speed, HashMap<String, String> altitude,
//                                    HashMap<String, String> status, HashMap<String, String> airportCode);
                        }
                        JSONArray arrivals = obj.getJSONArray("arrival");

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }



            }catch (Exception ex){
                Log.e("Crash!!", ex.getMessage() );
            }
            return  "Finished task";
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
            // flightNo.setText(flight.getFlightNo());
            flightStatus.setText(flight.getStatus());
            return newView;
        }

        public long getItemId(int position) {
            return position;
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
                nextPage = new Intent(MainActivityFlightStatusTracker.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_news_feed:
                nextPage = new Intent(MainActivityFlightStatusTracker.this, MainActivityNewsFeed.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_new_york:
                nextPage = new Intent(MainActivityFlightStatusTracker.this, MainActivityNewYorkTimes.class);
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
