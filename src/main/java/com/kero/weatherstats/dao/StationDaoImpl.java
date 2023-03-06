package com.kero.weatherstats.dao;

import com.kero.weatherstats.model.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class StationDaoImpl implements StationDao{


    @Override
    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Station;");
            ResultSet rs = ps.executeQuery();

            Station station;
            while (rs.next()) {
                int stationID = rs.getInt(1);
                String station_name = rs.getString(2);
                String position = rs.getString(3);
                Double height = rs.getDouble(4);
                String setup_date = rs.getString(5);


                station = new Station(stationID, station_name, position, height, setup_date);
                stations.add(station);
            }

        } catch (SQLException e) {
            System.err.println("cannot access records (StationDaoImpl)");

        }
        return stations;
    }

    private Connection con;
}