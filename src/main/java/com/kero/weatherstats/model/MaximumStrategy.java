package com.kero.weatherstats.model;

import java.util.ArrayList;
public class MaximumStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Double> list) {
        if (list.isEmpty()) {
            return 0;
        }
        double max = list.get(0);
        for (double num : list) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }
}