package com.kero.weatherstats.controllers;

import com.kero.weatherstats.dao.StationDao;
import com.kero.weatherstats.dao.StationDaoImpl;
import com.kero.weatherstats.dao.WeatherDataDao;
import com.kero.weatherstats.dao.WeatherDataDaoImpl;
import com.kero.weatherstats.model.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeatherController {

    private Calculator calculator;
    private ArrayList<WeatherData> weatherData;
    private ArrayList<Station> stations;

    private final ObservableList<Station> stationObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> typeObservableList = FXCollections.observableArrayList();

    @FXML private LineChart<String, Double> lineChart;

    @FXML private VBox weatherVbox;
    @FXML private VBox stationVbox;

    @FXML private MFXDatePicker startDate;
    @FXML private MFXDatePicker endDate;

    @FXML private MFXComboBox<Station> stationComboBox;
    @FXML private MFXComboBox<String> typeComboBox;

    public void initialize() {
        WeatherDataDao weatherDataDao = new WeatherDataDaoImpl();
        StationDao stationDao = new StationDaoImpl();

        weatherData = weatherDataDao.getAllWeatherData();
        stations = stationDao.getAllStations();

        calculator = new Calculator(new AverageStrategy());

        stationObservableList.addAll(stations);
        stationComboBox.setItems(stationObservableList);
        typeObservableList.addAll("Temperature", "Precipitation", "Wind", "Sunshine", "Cloud cover", "Cloud height");
        typeComboBox.setItems(typeObservableList);

        stationComboBox.setValue(stationComboBox.getItems().get(0));
        typeComboBox.setValue(typeComboBox.getItems().get(0));
        startDate.setValue(LocalDate.of(2023, 1, 1));
        endDate.setValue(LocalDate.of(2023, 1, 31));

        stationComboBox.setOnAction(e -> updateChartData());
        typeComboBox.setOnAction(e -> updateChartData());

        startDate.setOnAction(e -> {
            if(startDate.getValue().isAfter(endDate.getValue())) {
                endDate.setValue(startDate.getValue()); // Adjust end date if start date is after end date
            }
            updateChartData();
        });
        endDate.setOnAction(e -> {
            if(startDate.getValue().isAfter(endDate.getValue())) {
                startDate.setValue(endDate.getValue()); // Adjust start date if end date is before start date
            }
            updateChartData();
        });

        updateChartData();
    }

    private void updateChartData() {

        weatherVbox.getChildren().clear();
        stationVbox.getChildren().clear();

        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();
        XYChart.Series<String, Double> series3 = new XYChart.Series<>();
        Axis<String> xAxis = lineChart.getXAxis();
        Axis<Double> yAxis = lineChart.getYAxis();

        // Get data range
        ArrayList<WeatherData> dataRange = new ArrayList<>();
        for (WeatherData data : weatherData) {
            if(data.getStationID() == stationComboBox.getValue().getStationID()
                    && data.getData_Time().isAfter(startDate.getValue().atStartOfDay().minusHours(1))
                    && data.getData_Time().isBefore(endDate.getValue().atStartOfDay().plusDays(1))) {
                dataRange.add(data);
            }
        }

        // If startDate and endDate difference is 0, then set the xAxis to show hours instead of days
        boolean isSameDay = startDate.getValue().getDayOfMonth() == endDate.getValue().getDayOfMonth();
        if(isSameDay) {
            xAxis.setLabel("Hour of the day");
        } else {
            xAxis.setLabel("Day of the month");
        }

        // Set the data for the stationVbox
        stationVbox.getChildren().add(new Label("Name: " + stationComboBox.getValue().getStation_name()));
        stationVbox.getChildren().add(new Label("Location: " + stationComboBox.getValue().getPosition()));
        stationVbox.getChildren().add(new Label("Height: " + stationComboBox.getValue().getHeight()));
        stationVbox.getChildren().add(new Label("Setup date: " + stationComboBox.getValue().getSetup_date()));

        switch (typeComboBox.getValue()) {

            case "Temperature" -> {
                series1.setName("Minimum temperature");
                series2.setName("Average temperature");
                series3.setName("Maximum temperature");
                yAxis.setLabel("Celsius");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getMin_temp()));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getAvg_temp()));
                        series3.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getMax_temp()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getMin_temp()));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getAvg_temp()));
                        series3.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getMax_temp()));
                    }
                }
                lineChart.getData().setAll(series1, series2, series3);

                // Set the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getMin_temp());
                }
                ArrayList<Double> tempData2 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData2.add(data.getAvg_temp());
                }
                ArrayList<Double> tempData3 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData3.add(data.getMax_temp());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f C", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f C", calculator.calculate(tempData2, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f C", calculator.calculate(tempData3, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f C", calculator.calculate(tempData2, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f C", calculator.calculate(tempData2, new AverageMedianStrategy()))));
            }

            case "Precipitation" -> {
                series1.setName("Precipitation");
                yAxis.setLabel("Minutes");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getPrecip()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getPrecip()));
                    }
                }
                lineChart.getData().setAll(series1);

                // Set the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getPrecip());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f minutes", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f minutes", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f minutes", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f minutes", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f minutes", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Wind" -> {
                series1.setName("Average wind speed");
                series2.setName("Maximum wind speed");
                yAxis.setLabel("Meters per second");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getAvg_windspeed()));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getMax_windspeed()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getAvg_windspeed()));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getMax_windspeed()));
                    }
                }
                lineChart.getData().setAll(series1, series2);

                // Set the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getAvg_windspeed());
                }
                ArrayList<Double> tempData2 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData2.add(data.getMax_windspeed());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f m/s", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f m/s", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f m/s", calculator.calculate(tempData2, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f m/s", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f m/s", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Sunshine" -> {
                series1.setName("Sunshine");
                yAxis.setLabel("Strength");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getSunshine()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), data.getSunshine()));
                    }
                }
                lineChart.getData().setAll(series1);

                // Set the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getSunshine());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Cloud cover" -> {
                series1.setName("Cloud cover");
                yAxis.setLabel("Percentage");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), (double) data.getCloud_cover()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), (double) data.getCloud_cover()));
                    }
                }
                lineChart.getData().setAll(series1);

                // Set the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add((double) data.getCloud_cover());
                }
                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f%%", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f%%", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f%%", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f%%", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f%%", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Cloud height" -> {
                series1.setName("Cloud height");
                yAxis.setLabel("Meters");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), (double) data.getCloud_height()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), (double) data.getCloud_height()));
                    }
                }
                lineChart.getData().setAll(series1);

                // Set the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add((double) data.getCloud_height());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f m", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f m", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f m", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f m", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f m", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }
        }
    }
}
