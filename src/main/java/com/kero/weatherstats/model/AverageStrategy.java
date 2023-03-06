package com.kero.weatherstats.model;

import java.util.ArrayList;

public class AverageStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Integer> list) {
        // Check if list is empty
        if (list.isEmpty()) {
            return 0;
        }
        // Sum up all the elements in the list
        int sum = 0;
        for (int num : list) {
            sum += num;
        }
        // Returns the average as the sum divided by the size of the list
        return (double) sum / list.size();
    }
}
