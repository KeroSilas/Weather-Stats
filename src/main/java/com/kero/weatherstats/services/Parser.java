package com.kero.weatherstats.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

    // A method that takes a file name as input and returns an ArrayList of ArrayLists containing the data
    // The file must be tab-delimited
    public static ArrayList<ArrayList<String>> parseFile(String fileName) {

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(fileName));

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split("\t", -1);

                ArrayList<String> row = new ArrayList<>(Arrays.asList(tokens));
                data.add(row);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }
}
