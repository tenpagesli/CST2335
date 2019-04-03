/**
 * @author      Yan Qu <qu000021@algonquinlive.com>
 * @version     2019.03.20
 * @since       1.8
 */
package com.cst2335.queeny;

import android.content.DialogInterface;
import android.content.Intent;
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
        pbar.setVisibility(View.INVISIBLE);
        pbar.setProgress(10);

        // get all the views and buttons
        flightNoView = findViewById(R.id.flightNoD_qy_qy);
        locationView = findViewById(R.id.location_qy);
        speedView = findViewById(R.id.speed_qy);
        altitudeView = findViewById(R.id.altitude_qy);
        StatusView = findViewById(R.id.Status_qy);
        Button saveFlight = findViewById(R.id.save_btn);
        Button deleteFlight = findViewById(R.id.delete_btn);

        // get flight number and depIcao code from previous page
        Intent previousPage = getIntent();
        String flightNum = previousPage.getStringExtra("flightNo");
        String depIcao = previousPage.getStringExtra("depIcao");
        myUrl = preUrl + "&depIcao=" + depIcao + "&flightNum="+flightNum;

        // search for flight details, and display
        FlightDetailQuery fdq = new FlightDetailQuery();
        fdq.execute(myUrl); // it will go to doInBackground()

        // when click on save button
        saveFlight.setOnClickListener(c->{
            Toast.makeText(FlightDetailsActivity.this, "The flight has already saved", Toast.LENGTH_LONG).show();
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
                // update the view list here
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


        private boolean isExist(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();

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
                flight= new Flight(flightNo, location, departure, arrival, speed,  altitude,  status);
            }
        }
    }


}
