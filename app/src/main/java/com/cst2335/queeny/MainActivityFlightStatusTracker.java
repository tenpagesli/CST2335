/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.cst2335.MyUtil;
import com.cst2335.R;
import com.cst2335.hung.MainActivityNewsFeed;
import com.cst2335.kevin.MainActivityNewYorkTimes;
import com.cst2335.ryan.MainActivityDictionary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivityFlightStatusTracker extends AppCompatActivity {
    /** toolbar  */
    Toolbar tBar;
    /**  flight list */
    private ArrayList<Flight> allFlights = new ArrayList<>();
    /** the adapter to inflate the flights */
    private FlightAdapter flightAdapter;
    /**  progress bar  */
    private  ProgressBar pbar;
    /**  the flight number and flight status */
    private TextView flightNo_qy, flightStatus_qy;
    /**  activity name */
    protected static final String ACTIVITY_NAME = "Flights Search By Airport";
    /**  flight url */
    // protected static final String myUrl = "http://aviation-edge.com/v2/public/flights?key=8b238e-5c48c5&arrIata=MEX&limit=3";
    protected String preUrl = "http://aviation-edge.com/v2/public/flights?key=8b238e-5c48c5";
    protected String depAirCode, arrAirCode;
    protected String depUrl, arrUrl;

    /** airport code test view */
    private TextView airportCodeBtn;
    private Button searchBtn;
    private Button viewSavedBtn;
    private Button refreshList;
    private ListView flightList;
    /** switch button */
    Switch swBtn;
    /** airport code type*/
    String airportCodeType;
    /** depart flight list */
    ArrayList<Flight> depFlights = new ArrayList<>();
    /** arrive flight list */
    ArrayList<Flight> arrFlights = new ArrayList<>();

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

        airportCodeBtn = findViewById(R.id.airport_code_qy);
        searchBtn = findViewById(R.id.buttonSeach_qy);
        viewSavedBtn = findViewById(R.id.buttonView_qy);
        refreshList = findViewById(R.id.buttonRefresh_qy);
        flightList = findViewById(R.id.FlightList_qy);
        swBtn = findViewById(R.id.airport_switch_qy);

        // default airport code is IATA
        depAirCode = "&depIata=" + airportCodeBtn.getText().toString();
        arrAirCode ="&arrIata=" + airportCodeBtn.getText().toString();
        depUrl = preUrl + depAirCode;
        arrUrl = preUrl + arrAirCode;

        // switch button
        swBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) { // if change airport code
                // The toggle is enabled
                swBtn.setText("ICAO Code");
                airportCodeType = "ICAO";
                depAirCode = "&depIcao=" + airportCodeBtn.getText().toString();
                arrAirCode ="&arrIcao=" + airportCodeBtn.getText().toString();
            } else {
                // The toggle is disabled
                swBtn.setText("IATA Code");
                airportCodeType = "IATA";
                depAirCode = "&depIata=" + airportCodeBtn.getText().toString();
                arrAirCode ="&arrIata=" + airportCodeBtn.getText().toString();
            }
            depUrl = preUrl + depAirCode;
            arrUrl = preUrl + arrAirCode;

        });

        // when click on search button
        searchBtn.setOnClickListener(c->{
            // show progress bar
            pbar = findViewById(R.id.progress_Bar);
            pbar.setVisibility(View.VISIBLE);
            // get flight list from website
            FlightQuery fq = new FlightQuery();
            fq.execute(depUrl, arrUrl); // will run doInBackground()
            // inflate flight summary xml
            flightAdapter = new FlightAdapter(allFlights);
            flightList.setAdapter(flightAdapter);
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
        viewSavedBtn.setOnClickListener(c->{
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
                            pbar.setVisibility(View.VISIBLE);
                            /// get flight list from website
                            FlightQuery fq = new FlightQuery();
                            fq.execute(depUrl, arrUrl); // will run doInBackground()
                            // inflate flight summary xml
                            flightAdapter = new FlightAdapter(allFlights);
                            flightList.setAdapter(flightAdapter);
                            // after progress bar shown up, update the flight list
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
        @Override
        protected String doInBackground(String... strings) {
            //get the string url:
            String depUrl = strings[0];
            String arrUrl = strings[1];
            Log.e("deparURL: ", depUrl);
            Log.e("arrUrl: ", arrUrl);
            try {
                this.getFlights(depUrl, "depart");
                publishProgress(25); //tell android to call onProgressUpdate with 25 as parameter
                pbar.setProgress(25);
                Thread.sleep(1000);
                this.getFlights(arrUrl, "arrive");
                publishProgress(50); //tell android to call onProgressUpdate with 25 as parameter
                pbar.setProgress(50);
                Thread.sleep(1000);
                // merge flight lists
                allFlights = depFlights;
                publishProgress(75); //tell android to call onProgressUpdate with 25 as parameter
                pbar.setProgress(75);
                Thread.sleep(1000);
                allFlights.addAll(arrFlights);
                publishProgress(100); //tell android to call onProgressUpdate with 25 as parameter
                pbar.setProgress(100);
                Thread.sleep(1000);
            }catch (Exception ex){
                Log.e("Crash!!", ex.getMessage() );
            }
            return  "Finished task";
        }

        @Override
        protected  void onProgressUpdate(Integer ... value){
            // pbar.setVisibility(View.VISIBLE);
            Log.e("onProgressUpdate:", " should update some thing here");
            // update the view list here
        }

        @Override
        protected  void onPostExecute(String args){
            Log.e("onPostExecute:", " should update some thing here");
            pbar.setVisibility(View.INVISIBLE);

        }


        private boolean isExist(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();

        }

        private void getFlights(String myUrl, String flightType) throws MalformedURLException, IOException, JSONException {
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
            array = new JSONArray(sb.toString());
            for(int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                HashMap<String, String> flightNo = new HashMap<>();
                flightNo.put("iataNumber",obj.getJSONObject("flight").getString("iataNumber"));
                flightNo.put("icaoNumber",obj.getJSONObject("flight").getString("icaoNumber"));
                flightNo.put("number",obj.getJSONObject("flight").getString("number"));
                HashMap<String, String>  departure = new HashMap<>();
                departure.put("iataCode",obj.getJSONObject("departure").getString("iataCode"));
                departure.put("icaoCode",obj.getJSONObject("departure").getString("icaoCode"));
                HashMap<String, String>  arrival = new HashMap<>();
                departure.put("iataCode",obj.getJSONObject("arrival").getString("iataCode"));
                departure.put("icaoCode",obj.getJSONObject("arrival").getString("icaoCode"));
                HashMap<String, String>  speed = new HashMap<>();
                departure.put("horizontal",obj.getJSONObject("speed").getString("horizontal"));
                departure.put("isGround",obj.getJSONObject("speed").getString("isGround"));
                departure.put("vertical",obj.getJSONObject("speed").getString("vertical"));
                String  altitude = obj.getJSONObject("geography").getString("altitude");
                String status = obj.getString("status");
                Flight flight= new Flight(flightNo, departure, arrival, speed,  altitude,  status);
                if(flightType.equals("depart")){
                    depFlights.add(flight);
                }
                if(flightType.equals("arrive")){
                    arrFlights.add(flight);
                }
            }
        }
    }



    /**
     * This is a inner adapter class for the view list
     */
    protected class FlightAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;

        //Keep a reference to the data:
        public FlightAdapter(List<E> originalData) {
            dataCopy = originalData;
        }

        //You can give it an array
        public FlightAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }

        public FlightAdapter() {
        }

        @Override
        public int getCount() {
            return allFlights.size();
        }

        public Flight getItem(int position) {
            return allFlights.get(position);
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = MainActivityFlightStatusTracker.this.getLayoutInflater();
            View newView = null;
            Flight flight = getItem(position);
            newView = inflater.inflate(R.layout.activity_flight_summary_qy, null);
            TextView flightNo = newView.findViewById(R.id.v_flightNo_qy);
            TextView iataNumber_qy = newView.findViewById(R.id.v_iataNumber_qy);
            TextView icaoNumber_qy = newView.findViewById(R.id.v_icaoNumber_qy);
            TextView flightStatus = newView.findViewById(R.id.v_flightStatus_qy);
            HashMap<String, String> flightNoMap = flight.getFlightNo();
            flightNo.setText(flightNoMap.get("number"));
            iataNumber_qy.setText(flightNoMap.get("iataNumber"));
            icaoNumber_qy.setText(flightNoMap.get("icaoNumber"));
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
