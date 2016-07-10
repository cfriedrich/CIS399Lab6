package edu.uoregon.cnf.tidetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.os.AsyncTask;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private SimpleDateFormat shortDateOutFormat = new SimpleDateFormat("yyyy/MM/dd");
    private final String FILE1NAME = "astoria_annual.xml";
    private final String FILE2NAME = "florence_annual.xml";
    private final String FILE3NAME = "goldbeach_annual.xml";
    private final String FILE4NAME = "southbeach_annual.xml";

    private String[] files = {
            FILE1NAME,
            FILE2NAME,
            FILE3NAME,
            FILE4NAME
    };

    private String[] locations = {
            FILE1NAME.substring(0, 3),
            FILE2NAME.substring(0, 3),
            FILE3NAME.substring(0, 3),
            FILE4NAME.substring(0, 3)
    };

    private Spinner locationSpinner;
    private Button tideInfoButton;
    private DatePicker readingDatePicker;
    private FileIO fileIO;
    private ArrayList<DataItem> dataCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Intent intent = getIntent();

        fileIO = new FileIO(getApplicationContext());

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.locations_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        locationSpinner.setAdapter(adapter);

        locationSpinner.setSelection(0);

        tideInfoButton = (Button) findViewById(R.id.tideInfoButton);
        tideInfoButton.setOnClickListener(this);

        readingDatePicker = (DatePicker) findViewById(R.id.readingDatePicker);

        Boolean returningFlag = Boolean.parseBoolean(intent.getStringExtra("returning"));

        if(returningFlag == true) {
            dataCollection = fileIO.readAllFiles(files);
            // get db and StringBuilder objects
            TideTrackerDB db = new TideTrackerDB(this);
            db.fillData(db, locations, dataCollection);
        }
//        new ReadFeed().execute();

    }

//    class ReadFeed extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... params) {
//
//
//            return null;
//        }
//    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tideInfoButton)
        {
            if(locationSpinner.getSelectedItemPosition() == 0)
            {
                Toast toast = Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                String location = locationSpinner.getSelectedItem().toString();

                if(readingDatePicker.getYear() != 2016)
                {
                    Toast toast = Toast.makeText(this, "Please select a date within 2016", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    int day = readingDatePicker.getDayOfMonth();
                    int month = readingDatePicker.getMonth();
                    int year =  readingDatePicker.getYear();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);

                    Date selectedDate =  calendar.getTime();

                    String formattedDate = shortDateOutFormat.format(selectedDate);

                    Intent intent = new Intent(this, ViewTidesActivity.class);

                    intent.putExtra("location", location);
                    intent.putExtra("date", formattedDate);

                    this.startActivity(intent);

                }
            }
        }
    }
}
