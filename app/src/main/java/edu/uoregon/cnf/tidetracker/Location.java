package edu.uoregon.cnf.tidetracker;

/**
 * Created by Christopher on 7/9/2016.
 */
public class Location {

    private int locationID;
    private String name;

    public Location ()
    {
        name = "";
    }

    public Location(String name)
    {
        this.name = name;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        locationID = locationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
