package com.kero.weatherstats.controllers;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WeatherController {

    @FXML private VBox chartPane;
    @FXML private HBox hBox;

    @FXML private Label medianLabel;
    @FXML private Label averageLabel;
    @FXML private Label averageMedianLabel;

    @FXML private MFXDatePicker endDate;
    @FXML private MFXDatePicker startDate;

    @FXML private MFXComboBox<?> stationComboBox;
    @FXML private MFXComboBox<?> typeComboBox;

}
