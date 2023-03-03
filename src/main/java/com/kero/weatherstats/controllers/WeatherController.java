package com.kero.weatherstats.controllers;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import java.time.LocalDate;

public class WeatherController {

    @FXML private LineChart<String, Number> lineChart;

    @FXML private Label medianLabel;
    @FXML private Label averageLabel;
    @FXML private Label averageMedianLabel;

    @FXML private MFXDatePicker startDate;
    @FXML private MFXDatePicker endDate;

    @FXML private MFXComboBox<?> stationComboBox;
    @FXML private MFXComboBox<?> typeComboBox;

    public void initialize() {

        // --- LineChart configurations --- //

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Axis<String> xAxis = lineChart.getXAxis();
        Axis<Number> yAxis = lineChart.getYAxis();

        series.setName("Average temperature");
        xAxis.setLabel("Day of the month");
        yAxis.setLabel("Temperature");
        /*for (int i = 0; i < -DATA-; i++) {
            series.getData().add(new XYChart.Data<>(DayOfWeek.of(i+1).name(), -DATA-.get(i)));
        }*/
        lineChart.getData().add(series);

        // --- ComboBox configurations --- //

        stationComboBox.setOnAction(e -> {

        });

        typeComboBox.setOnAction(e -> {

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

        // --- Label configurations --- //

        medianLabel.setText("Median: " + "-DATA-");
        averageLabel.setText("Average: " + "-DATA-");
        averageMedianLabel.setText("Average Median: " + "-DATA-");
    }

}
