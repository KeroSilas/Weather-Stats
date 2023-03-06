package com.kero.weatherstats.dao;

import com.kero.weatherstats.model.WeatherData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class WeatherDataDaoImpl implements WeatherDataDao{
    @Override
    public List<WeatherData> getAllWeatherData() {
        List<WeatherData> weatherdata = new ArrayList();
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM WeatherData;");
            ResultSet rs = ps.executeQuery();

            WeatherData weatherData;
            while (rs.next()) {
                int weatherID = rs.getInt(0);
                int stationID = rs.getInt(1);
                Date data_time = rs.getDate(2);
                Double precip = rs.getDouble(3);
                Double precip_minutes = rs.getDouble(4);
                Double avg_temp = rs.getDouble(5);
                Double max_temp = rs.getDouble(6);
                Double min_temp = rs.getDouble(7);
                Double sunshine = rs.getDouble(8);
                Double avg_windspeed = rs.getDouble(9);
                Double max_windspeed = rs.getDouble(10);
                int cloud_height = rs.getInt(11);
                int cloud_cover = rs.getInt(12);


                weatherData = new WeatherData(weatherID, stationID, data_time, precip, precip_minutes, avg_temp, max_temp, min_temp, sunshine, avg_windspeed, max_windspeed, cloud_height, cloud_cover);
                weatherdata.add(weatherData);
            }

        } catch (SQLException e) {
            System.err.println("cannot access records (WeatherDataDaoImpl)");

        }
        return weatherdata;
    }

    private Connection con;
}

