package com.kero.weatherstats.model;

import java.time.LocalDateTime;
import java.util.Date;

public class WeatherData {

private final int weatherID;
private final int stationID;
private final LocalDateTime data_Time;
private final double precip;
private final double precip_minutes;
private final double avg_temp;
private final double max_temp;
private final double min_temp;
private final double sunshine;
private final double avg_windspeed;
private final double max_windspeed;
private final int cloud_height;
private final int cloud_cover;

    public WeatherData(int weatherID, int stationID, LocalDateTime data_Time, double precip, double precip_minutes, double avg_temp, double max_temp, double min_temp, double sunshine, double avg_windspeed, double max_windspeed, int cloud_height, int cloud_cover) {
        this.weatherID = weatherID;
        this.stationID = stationID;
        this.data_Time = data_Time;
        this.precip = precip;
        this.precip_minutes = precip_minutes;
        this.avg_temp = avg_temp;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.sunshine = sunshine;
        this.avg_windspeed = avg_windspeed;
        this.max_windspeed = max_windspeed;
        this.cloud_height = cloud_height;
        this.cloud_cover = cloud_cover;
    }

    public int getWeatherID() {
        return weatherID;
    }

    public int getStationID() {
        return stationID;
    }

    public LocalDateTime getData_Time() {
        return data_Time;
    }

    public double getPrecip() {
        return precip;
    }

    public double getPrecip_minutes() {
        return precip_minutes;
    }

    public double getAvg_temp() {
        return avg_temp;
    }

    public double getMax_temp() {
        return max_temp;
    }

    public double getMin_temp() {
        return min_temp;
    }

    public double getSunshine() {
        return sunshine;
    }

    public double getAvg_windspeed() {
        return avg_windspeed;
    }

    public double getMax_windspeed() {
        return max_windspeed;
    }

    public int getCloud_height() {
        return cloud_height;
    }

    public int getCloud_cover() {
        return cloud_cover;
    }
}
