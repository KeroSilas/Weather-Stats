package com.kero.weatherstats.dao;

import com.kero.weatherstats.model.WeatherData;

import java.util.List;

public interface WeatherDataDao {

    //Reads all WeatherData from the database
    public List<WeatherData> getAllWeatherData();

}
