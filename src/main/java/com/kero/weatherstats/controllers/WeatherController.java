package com.kero.weatherstats.controllers;

import com.kero.weatherstats.dao.StationDao;
import com.kero.weatherstats.dao.StationDaoImpl;
import com.kero.weatherstats.dao.WeatherDataDao;
import com.kero.weatherstats.dao.WeatherDataDaoImpl;
import com.kero.weatherstats.model.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.application.Platform;
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

    @FXML private LineChart<Integer, Double> lineChart;

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

        // --- ComboBox configurations --- //

        stationObservableList.addAll(stations);
        stationComboBox.setItems(stationObservableList);
        typeObservableList.addAll("Temperature", "Precipitation", "Wind", "Sunshine", "Cloud cover", "Cloud height");
        typeComboBox.setItems(typeObservableList);

        Platform.runLater(() -> {
            stationComboBox.setValue(stationComboBox.getItems().get(0));
            typeComboBox.setValue(typeComboBox.getItems().get(0));
        });

        stationComboBox.setOnAction(e -> updateChartData());
        typeComboBox.setOnAction(e -> updateChartData());

        // --- DatePicker configurations --- //

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

        startDate.setValue(LocalDate.of(2023, 1, 1));
        endDate.setValue(LocalDate.of(2023, 1, 31));
    }

    private void updateChartData() {

        weatherVbox.getChildren().clear();
        stationVbox.getChildren().clear();

        XYChart.Series<Integer, Double> series1 = new XYChart.Series<>();
        XYChart.Series<Integer, Double> series2 = new XYChart.Series<>();
        XYChart.Series<Integer, Double> series3 = new XYChart.Series<>();
        Axis<Integer> xAxis = lineChart.getXAxis();
        Axis<Double> yAxis = lineChart.getYAxis();

        // Get data range
        ArrayList<WeatherData> dataRange = new ArrayList<>();
        for (WeatherData data : weatherData) {
            if(data.getStationID() == stationComboBox.getValue().getStationID()
                    && data.getData_Time().isAfter(startDate.getValue().atStartOfDay().minusHours(1))
                    && data.getData_Time().isBefore(endDate.getValue().atStartOfDay())) {
                dataRange.add(data);
                System.out.println(data.getData_Time() + " " + data.getStationID());
            }
        }

        // If startDate and endDate difference is 0, then set the xAxis to show hours instead of days
        boolean isSameDay = startDate.getValue().getDayOfMonth() == endDate.getValue().getDayOfMonth();
        int days = startDate.getValue().getDayOfMonth() - endDate.getValue().getDayOfMonth();
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
                        series1.getData().add(new XYChart.Data<>(data.getData_Time().getHour(), data.getMin_temp()));
                        series2.getData().add(new XYChart.Data<>(data.getData_Time().getHour(), data.getAvg_temp()));
                        series3.getData().add(new XYChart.Data<>(data.getData_Time().getHour(), data.getMax_temp()));
                    } else {
                        series1.getData().add(new XYChart.Data<>(data.getData_Time().getDayOfMonth(), data.getMin_temp()));
                        series2.getData().add(new XYChart.Data<>(data.getData_Time().getDayOfMonth(), data.getAvg_temp()));
                        series3.getData().add(new XYChart.Data<>(data.getData_Time().getDayOfMonth(), data.getMax_temp()));
                    }
                }

                lineChart.getData().setAll(series1, series2, series3);

                // Set the data for the weatherVbox
                weatherVbox.getChildren().add(new Label("Minimum temperature: "));
                weatherVbox.getChildren().add(new Label("Average temperature: "));
                weatherVbox.getChildren().add(new Label("Maximum temperature: "));
                weatherVbox.getChildren().add(new Label("Median temperature: "));
                weatherVbox.getChildren().add(new Label("Average median temperature: "));
            }

            case "Precipitation" -> {
                series1.setName("Precipitation");
                yAxis.setLabel("Minutes");

                lineChart.getData().setAll(series1);
            }

            case "Wind" -> {
                series1.setName("Average wind speed");
                series2.setName("Maximum wind speed");
                yAxis.setLabel("Meters per second");

                lineChart.getData().setAll(series1, series2);
            }

            case "Sunshine" -> {
                series1.setName("Sunshine");
                yAxis.setLabel("Strength");

                lineChart.getData().setAll(series1);
            }

            case "Cloud cover" -> {
                series1.setName("Cloud cover");
                yAxis.setLabel("Percentage");

                lineChart.getData().setAll(series1);
            }

            case "Cloud height" -> {
                series1.setName("Cloud height");
                yAxis.setLabel("Meters");

                lineChart.getData().setAll(series1);
            }
        }
    }
}
