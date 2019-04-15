/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;

public class FlightDetailsActivity extends AppCompatActivity {

    /** toolbar  */
    Toolbar tBar;
    /** progress bar */
    ProgressBar pbar;
    /** url */
    String myUrl = "";
    String preUrl = "http://aviation-edge.com/v2/public/flights?key=" + MyUtil.flightApiKey;
    /** all the views from page */
    TextView flightNoView;
    TextView locationView;
    TextView speedView;
    TextView altitudeView;
    TextView StatusView;
    /** flight object to save flight details */
    Flight flight;


    private SQLiteDatabase db;
    private FlightDataBaseHelper dbOpener;

    /**
     * When user want's to see the flight details, this method runs
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);
        // get toolbar
        tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        // get progress bar
        pbar = findViewById(R.id.progress_Bar);
        pbar.setProgress(10);

        // get all the views and buttons
        flightNoView = findViewById(R.id.flightNoD_qy_qy);
        locationView = findViewById(R.id.location_qy);
        speedView = findViewById(R.id.speed_qy);
        altitudeView = findViewById(R.id.altitude_qy);
        StatusView = findViewById(R.id.Status_qy);
        Button saveFlight = findViewById(R.id.save_btn);
        Button prePage = findViewById(R.id.previous_page_btn);
        // get delete button and set it as invisible because no need to delete the record yet
        Button deleteFlight = findViewById(R.id.delete_btn);
        deleteFlight.setVisibility(View.INVISIBLE);

        // get flight number and depIcao code from previous page
        Intent previousPage = getIntent();
        String flightNum = previousPage.getStringExtra("flightNo");
        String depIcao = previousPage.getStringExtra("depIcao");
        myUrl = preUrl + "&depIcao=" + depIcao + "&flightNum="+flightNum;

        // search for flight details, and display
        FlightDetailQuery fdq = new FlightDetailQuery();
        fdq.execute(myUrl); // it will go to doInBackground()

        // when click on save button

        //get a database
        dbOpener= new FlightDataBaseHelper(this);
        db = dbOpener.getWritableDatabase();
        saveFlight.setOnClickListener(c->{
            // add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string message in the message column
            newRowValues.put(FlightDataBaseHelper.COL_ALTITUDE, flight.getAltitude());
            newRowValues.put(FlightDataBaseHelper.COL_FLIGHT_NO, flight.getFlightNo().get("number"));
            newRowValues.put(FlightDataBaseHelper.COL_HORIZONTAL, flight.getSpeed().get("horizontal"));
            newRowValues.put(FlightDataBaseHelper.COL_ISGROUND, flight.getSpeed().get("isGround"));
            newRowValues.put(FlightDataBaseHelper.COL_LATITUDE, flight.getLocation().get("latitude"));
            newRowValues.put(FlightDataBaseHelper.COL_LONGITUDE, flight.getLocation().get("longitude"));
            newRowValues.put(FlightDataBaseHelper.COL_STATUS, flight.getStatus());
            newRowValues.put(FlightDataBaseHelper.COL_VERTICAL, flight.getSpeed().get("vertical"));

            //insert to the database
            long newId = db.insert(FlightDataBaseHelper.TABLE_NAME,null,newRowValues);
            String taostMessage;
            if(newId>0){
                taostMessage ="Flight information saved successfully!";
            }else{
                taostMessage ="Sorry, flight information saved unsuccessfully!";
            }
            Toast.makeText(FlightDetailsActivity.this, taostMessage, Toast.LENGTH_LONG).show();
        });

        // when click on go to previous page
        prePage.setOnClickListener(c->{
            Intent nextPage = new Intent(FlightDetailsActivity.this, MainActivityFlightStatusTracker.class);
            startActivity(nextPage);
        });
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
                nextPage = new Intent(FlightDetailsActivity.this, MainActivityDictionary.class);
                startActivity(nextPage);
                break;
            //when click on "news feed"
            case R.id.go_news_feed:
                nextPage = new Intent(FlightDetailsActivity.this, MainActivityNewsFeed.class);
                startActivity(nextPage);
                break;
            //when click on "new york times"
            case R.id.go_new_york:
                nextPage = new Intent(FlightDetailsActivity.this, MainActivityNewYorkTimes.class);
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

    /**
     * This is a inner class to get flight details
     */
    class FlightDetailQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //get the string url:
            String depUrl = strings[0];
            Log.e("deparURL: ", depUrl);
            try {
                this.getFlightDetails(depUrl);
                publishProgress(1); //tell android to call onProgressUpdate with 25 as parameter

                pbar.setVisibility(View.VISIBLE);
                pbar.setProgress(50);
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
            if(value[0]==1){
                // update the views here
                flightNoView.setText("Flight Number: " + flight.getFlightNo().get("number"));
                String locationText = "Location: " + "<br/>"
                                      + "&nbsp &nbsp &nbsp Latitude: " + flight.getLocation().get("latitude") + "<br/>"
                                      + "&nbsp &nbsp &nbsp  Longitude: " + flight.getLocation().get("longitude");
                locationView.setText(Html.fromHtml(locationText));
                String speedText = "Speed: " + "<br/>"
                        + "&nbsp &nbsp &nbsp horizontal: " + flight.getSpeed().get("horizontal") + "<br/>"
                        + "&nbsp &nbsp &nbsp isGround: " + flight.getSpeed().get("isGround") + "<br/>"
                        + "&nbsp &nbsp &nbsp vertical: " + flight.getSpeed().get("vertical");
                speedView.setText(Html.fromHtml(speedText));
                altitudeView.setText("Altitude: " + flight.getAltitude());
                StatusView.setText("Status: " + flight.getStatus());
                pbar.setProgress(100);
            }
        }

        @Override
        protected  void onPostExecute(String args){
            Log.e("onPostExecute:", " should update some thing here");
            pbar.setVisibility(View.INVISIBLE);
        }

        private void getFlightDetails(String myUrl) throws MalformedURLException, IOException, JSONException {
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
                arrival.put("iataCode",obj.getJSONObject("arrival").getString("iataCode"));
                arrival.put("icaoCode",obj.getJSONObject("arrival").getString("icaoCode"));
                HashMap<String, String>  speed = new HashMap<>();
                speed.put("horizontal",obj.getJSONObject("speed").getString("horizontal"));
                speed.put("isGround",obj.getJSONObject("speed").getString("isGround"));
                speed.put("vertical",obj.getJSONObject("speed").getString("vertical"));
                String  altitude = obj.getJSONObject("geography").getString("altitude");
                HashMap<String, String> location = new HashMap<>();
                location.put("latitude",obj.getJSONObject("geography").getString("latitude") );
                location.put("longitude",obj.getJSONObject("geography").getString("longitude") );
                String status = obj.getString("status");
                flight= new Flight(0, flightNo, location, departure, arrival, speed,  altitude,  status);
            }
        }
    }
}
