package edu.uoregon.cnf.tidetracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import android.content.Context;
import android.widget.SectionIndexer;
import android.widget.SimpleAdapter;

import edu.uoregon.cnf.tidetracker.R;

public class ListAdapter
        extends SimpleAdapter implements SectionIndexer {

    private HashMap<String, Integer> mapIndex;
    private String[] months;
    private ArrayList<HashMap<String, String>> items;
    private SimpleDateFormat dateInFormat = new SimpleDateFormat("yyyy/MM/dd EEEE");
    private SimpleDateFormat monthOutFormat = new SimpleDateFormat("MMMM");

    public ListAdapter(Context context, ArrayList<HashMap<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.items = data;

        mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < items.size(); i++) {
            HashMap<String, String> thisMap = items.get(i);
            String dateString = thisMap.values().toArray()[1].toString();
            String monthName = "";
            try {
                Date date = dateInFormat.parse(dateString.trim());
                 monthName = monthOutFormat.format(date);
            } catch (ParseException e) {

            }
            if(monthName == "")
            {
                monthName = monthOutFormat.format(new Date());
            }

            mapIndex.put(monthName, i);
        }

        Set<String> sectionLetters = mapIndex.keySet();

        // Create a list from the set to sort
        ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

        months = new String[sectionList.size()];

        sectionList.toArray(months);
    }

    public int getPositionForSection(int section) {
        return mapIndex.get(months[section]);
    }

    public int getSectionForPosition(int position) {
        return 0;
    }

    public Object[] getSections() {
        return months;
    }
}
