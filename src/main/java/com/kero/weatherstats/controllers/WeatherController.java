package com.kero.weatherstats.controllers;

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
import java.time.LocalDate;

public class WeatherController {

    private final ObservableList<String> stationObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> typeObservableList = FXCollections.observableArrayList();

    @FXML private LineChart<String, Number> lineChart;

    @FXML private Label medianLabel;
    @FXML private Label averageLabel;
    @FXML private Label averageMedianLabel;

    @FXML private MFXDatePicker startDate;
    @FXML private MFXDatePicker endDate;

    @FXML private MFXComboBox<String> stationComboBox;
    @FXML private MFXComboBox<String> typeComboBox;

    public void initialize() {

        // --- ComboBox configurations --- //

        //stationObservableList.addAll(DATA);
        stationComboBox.setItems(stationObservableList);
        typeObservableList.addAll("Temperature", "Precipitation", "Wind", "Sunshine", "Cloud cover", "Cloud height");
        typeComboBox.setItems(typeObservableList);

        stationComboBox.setOnAction(e -> updateChartData());
        typeComboBox.setOnAction(e -> updateChartData());

        Platform.runLater(() -> {
            //stationComboBox.setValue("First station in list");
            typeComboBox.setValue("Temperature");
        });

        // --- DatePicker configurations --- //

        startDate.setValue(LocalDate.of(2023, 1, 1));
        endDate.setValue(LocalDate.of(2023, 1, 31));

        startDate.setOnAction(e -> {
            if(startDate.getValue().isAfter(endDate.getValue())) {
                endDate.setValue(startDate.getValue()); // Adjust end date if start date is after end date
            }
        });

        endDate.setOnAction(e -> {
            if(startDate.getValue().isAfter(endDate.getValue())) {
                startDate.setValue(endDate.getValue()); // Adjust start date if end date is before start date
            }
        });
    }

    private void updateChartData() {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        Axis<String> xAxis = lineChart.getXAxis();
        Axis<Number> yAxis = lineChart.getYAxis();
        xAxis.setLabel("Day of the month");

        switch (typeComboBox.getValue()) {
            case "Temperature" -> {
                series1.setName("Minimum temperature");
                series2.setName("Average temperature");
                series3.setName("Maximum temperature");
                yAxis.setLabel("Celsius");

                /*for (int i = 0; i < DATA; i++) {
                    series1.getData().set(new XYChart.Data<>(DATA, DATA));
                }*/
                lineChart.getData().setAll(series1, series2, series3);
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
