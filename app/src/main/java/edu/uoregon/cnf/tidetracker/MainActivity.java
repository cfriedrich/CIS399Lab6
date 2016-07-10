package edu.uoregon.cnf.tidetracker;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener {
    private ParsedData dataCollection;
    private FileIO fileIO;
    private TextView titleTextView;
    private ListView readingsListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileIO = new FileIO(getApplicationContext());

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        readingsListView = (ListView) findViewById(R.id.readingsListView);

        readingsListView.setOnItemClickListener(this);


        new ReadFeed().execute();
    }

    class ReadFeed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
//            dataCollection = fileIO.readFile();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Update the display
            MainActivity.this.updateDisplay();
        }
    }


    public void updateDisplay()
    {
        if (dataCollection == null) {
            titleTextView.setText("Unable to read XML file");
            return;
        }

        // Set the Title
        titleTextView.setText("Tide Readings for Florence, Oregon");

        ArrayList<DataItem> dataItems = dataCollection.getAllItems();

        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();

        for (DataItem item : dataItems) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("date", item.getFormattedDate());
            map.put("line2", item.getHighlow() + " " + item.getTimeString());
            data.add(map);
        }

        // Create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "line2"};
        int[] to = {R.id.dateTextView, R.id.timeTextView};

        // Create and set the adapter

        ListAdapter adapter = new ListAdapter (this, data, resource, from, to);
        readingsListView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        DataItem item = dataCollection.getItem(position);

        Context context = getApplicationContext();
        CharSequence text = item.getFeet() + " ft, " + item.getCentimeters() + "cm";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
