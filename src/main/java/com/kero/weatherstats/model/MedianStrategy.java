package com.kero.weatherstats.model;

import java.util.ArrayList;
import java.util.Collections;

public class MedianStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Integer> list) {
        // Check if list is empty
        if (list.isEmpty()) {
            return 0;
        }
        // Sort the list in ascending order
        Collections.sort(list);
        int size = list.size();
        // If the size is odd, return the middle element as the median
        if (size % 2 == 1) {
            return list.get(size / 2);
        }
        // If the size is even, return the average of the middle two elements as the median
        else {
            return (list.get(size / 2 - 1) + list.get(size / 2)) / 2.0;
        }
    }
}
