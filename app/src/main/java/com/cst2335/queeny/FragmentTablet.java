package com.cst2335.queeny;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cst2335.R;

public class FragmentTablet extends Fragment {
    /** all the views from page */
    TextView flightNoView;
    TextView locationView;
    TextView speedView;
    TextView altitudeView;
    TextView StatusView;
    /** flight object to save flight details */
    Flight flight;

    public static FragmentTablet newInstance(boolean isTablet,Flight clickedFlight) {
        Bundle args = new Bundle();
        args.putBoolean("isTablet", isTablet);
        args.putLong("id", clickedFlight.getId());
        args.putString("flightNo", clickedFlight.getFlightNo().get("number"));
        args.putString("horizontal", clickedFlight.getSpeed().get("horizontal"));
        args.putString("altitude", clickedFlight.getAltitude());
        args.putString("isground", clickedFlight.getSpeed().get("isGround"));
        args.putString("latitude", clickedFlight.getLocation().get("latitude"));
        args.putString("longitude", clickedFlight.getLocation().get("longitude"));
        args.putString("status", clickedFlight.getStatus());
        args.putString("vertical", clickedFlight.getSpeed().get("vertical"));
        FragmentTablet fragmentTablet = new FragmentTablet();
        fragmentTablet.setArguments(args);
        return fragmentTablet;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_flight_details, container, false);

        // get all the views and buttons
        flightNoView = view.findViewById(R.id.flightNoD_qy_qy);
        locationView = view.findViewById(R.id.location_qy);
        speedView = view.findViewById(R.id.speed_qy);
        altitudeView = view.findViewById(R.id.altitude_qy);
        StatusView = view.findViewById(R.id.Status_qy);
        Button deleteFlight = view.findViewById(R.id.delete_btn);
        // get save and prePage button and set them as invisible because no need to use it
        Button prePage = view.findViewById(R.id.previous_page_btn);
        Button saveFlight = view.findViewById(R.id.save_btn);
        prePage.setVisibility(View.INVISIBLE);
        saveFlight.setVisibility(View.INVISIBLE);


        // update the views here
        String flightNo = "Flight Number: " + getArguments().getString("flightNo");
        flightNoView.setText(flightNo);
        String locationText = "Location: " + "<br/>"
                + "&nbsp &nbsp &nbsp Latitude: " + getArguments().getString("latitude") + "<br/>"
                + "&nbsp &nbsp &nbsp  Longitude: " + getArguments().getString("longitude");
        locationView.setText(Html.fromHtml(locationText));
        String speedText = "Speed: " + "<br/>"
                + "&nbsp &nbsp &nbsp horizontal: " + getArguments().getString("horizontal") + "<br/>"
                + "&nbsp &nbsp &nbsp isGround: " + getArguments().getString("isGround") + "<br/>"
                + "&nbsp &nbsp &nbsp vertical: " + getArguments().getString("vertical");
        speedView.setText(Html.fromHtml(speedText));
        String altitude = "Altitude: " + getArguments().getString("altitude");
        altitudeView.setText(altitude);
        String status = "Status: " + getArguments().getString("status");
        StatusView.setText(status);
        boolean isTablet = getArguments().getBoolean("isTablet");

        deleteFlight.setOnClickListener(v -> {
            Snackbar sb = Snackbar.make(deleteFlight, "You really want to delete?", Snackbar.LENGTH_LONG)
                    .setAction("Yes!", e -> {
                        if (isTablet) {
                            ((FlightSavedActivity) getActivity()).deleteMessage((int) getArguments().getLong("messageId"));
                            getFragmentManager().beginTransaction().remove(FragmentTablet.this).commit();
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra("id", getArguments().getLong("id"));
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();
                        }
                    });
            sb.show();
        });

        return view;
    }
}
