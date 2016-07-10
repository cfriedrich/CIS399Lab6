package edu.uoregon.cnf.tidetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewTidesActivity extends AppCompatActivity {

    private ArrayList<Prediction> datePredictions;
    private TideTrackerDB db;
    private ListView tidesListView;
    private String date;
    private String location;
    private int locationID;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tides);

        Intent intent = getIntent();

        date = intent.getStringExtra("date");
        location = intent.getStringExtra("location");
        String locationShort = location.toLowerCase().substring(0, 3);


        db = new TideTrackerDB(this);
        locationID = db.getLocationID(locationShort);

        tidesListView = (ListView) findViewById(R.id.tidesListView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        datePredictions = db.getPredictions(String.valueOf(locationID), date);

        dateTextView.setText("Readings for " + date);

        displayTideData();
    }

    private void displayTideData()
    {
        ArrayList<String[]> data =
                new ArrayList<String[]>();

        for (Prediction prediction : datePredictions) {
            String[] values = new String[] {
                prediction.getTime(),
                prediction.getHighlow(),
                prediction.getFeet(),
                prediction.getCentimeters()};
            data.add(values);
        }

        // Create the resource, from, and to variables
        int resource = R.layout.listview_tides;
        String[] from = {"date", "line2"};
        int[] to = {R.id.dateTextView, R.id.timeTextView};

        // Create and set the adapter

        ArrayAdapter<String[]> adapter = new ArrayAdapter(this, resource, data);
        tidesListView.setAdapter(adapter);
    }




}
