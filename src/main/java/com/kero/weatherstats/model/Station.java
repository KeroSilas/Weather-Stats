package com.kero.weatherstats.model;

public class Station {

    private int stationID;

    private String station_name;

    private String position;

    private double height;

    private String setup_date;

    public Station(int stationID, String station_name, String position, double height, String setup_date){
        this.stationID = stationID;
        this.station_name = station_name;
        this.position = position;
        this.height = height;
        this.setup_date = setup_date;
    }

    public int getStationID() {
        return stationID;
    }

    public String getStation_name() {
        return station_name;
    }

    public String getPosition() {
        return position;
    }

    public double getHeight() {
        return height;
    }

    public String getSetup_date() {
        return setup_date;
    }

    @Override
    public String toString() {
        return getStation_name();
    }
}
