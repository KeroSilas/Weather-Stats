package com.kero.weatherstats.model;

import java.util.ArrayList;
public class MinimumStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Double> list) {
        if (list.isEmpty()) {
            return 0;
        }
        double min = list.get(0);
        for (double num : list) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }
}