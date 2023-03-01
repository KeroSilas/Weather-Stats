package com.kero.weatherstats.services;

import com.kero.weatherstats.dao.WeatherDataImport;

import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        WeatherDataImport weatherImport = new WeatherDataImport();
        weatherImport.importToStation(Parser.parseFile(Objects.requireNonNull(Main.class.getResource("/com/kero/weatherstats/Station.txt")).getPath()));
        weatherImport.importToWeatherData(Parser.parseFile(Objects.requireNonNull(Main.class.getResource("/com/kero/weatherstats/WeatherData.txt")).getPath()));
    }
}