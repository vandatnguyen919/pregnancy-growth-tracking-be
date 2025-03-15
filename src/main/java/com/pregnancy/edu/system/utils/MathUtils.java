package com.pregnancy.edu.system.utils;

public class MathUtils {

    public static double round(int decimalPlaces, double value) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }
}
