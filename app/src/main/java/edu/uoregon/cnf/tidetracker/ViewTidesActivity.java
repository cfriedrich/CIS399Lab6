package edu.uoregon.cnf.tidetracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Selection;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewTidesActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Prediction> datePredictions;
    private TideTrackerDB db;
    private ListView tidesListView;
    private String date;
    private String location;
    private int locationID;
    private TextView dateTextView;
    private Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tides);
//        // my_child_toolbar is defined in the layout file
//        Toolbar myChildToolbar =
//                (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myChildToolbar);
//
//        // Get a support ActionBar corresponding to this toolbar
//        ActionBar ab = getSupportActionBar();
//
//        // Enable the Up button
//        ab.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        date = intent.getStringExtra("date");
        location = intent.getStringExtra("location");
        String locationShort = location.toLowerCase().substring(0, 3);

        returnButton = (Button) findViewById(R.id.returnButton);
        returnButton.setOnClickListener(this);

        db = new TideTrackerDB(this);

        locationID = db.getLocationID(locationShort);

        tidesListView = (ListView) findViewById(R.id.tidesListView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        datePredictions = db.getPredictions(String.valueOf(locationID), date);

        dateTextView.setText("Readings for " + location + ", Oregon on " + date);

        displayTideData();
    }

    private void displayTideData()
    {
//        ArrayList<String[]> data =
//                new ArrayList<String[]>();

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        for (Prediction prediction : datePredictions) {
            HashMap<String, String> map = new HashMap<String, String>();
                map.put("time", prediction.getTime());
                map.put("highlow", prediction.getHighlow());
                map.put("feet", prediction.getFeet());
                map.put("cm", prediction.getCentimeters());
            data.add(map);
        }

        // Create the resource, from, and to variables
        int resource = R.layout.listview_tides;
        String[] from = {"time", "highlow", "feet", "cm"};
        int[] to = {R.id.timeTextView, R.id.highLowTextView, R.id.feetTextView, R.id.centimetersTextView};

        // Create and set the adapter

        SimpleAdapter adapter = new SimpleAdapter(this, data, resource, from, to);
        tidesListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.returnButton)
        {
            Intent intent = new Intent(this, SelectionActivity.class);
            intent.putExtra("returning", "true");
            this.startActivity(intent);
        }
    }
}
