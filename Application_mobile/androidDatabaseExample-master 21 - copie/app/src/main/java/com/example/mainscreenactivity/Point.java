package com.example.mainscreenactivity;
import com.example.maps.api.MWMPoint;

/**
 * Created by Robert on 08/06/2017.
 */

public class Point {
    private  String name;
    private  double lat;
    private  double lon;

    public Point(String name, double lat, double lon)
    {
        this.name = name;
        this.lat = lat;
        this.lon = lon;

    }
    @Override
    public String toString()     { return name; }
    public MWMPoint toMWMPoint() { return new MWMPoint(lat, lon, name); }
    public String getName()        { return name; }
    public double getLat()         { return lat; }
    public double getLon()         { return lon; }
}