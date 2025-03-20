package com.pregnancy.edu.system.utils.generators;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.metric.Metric;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FetusMetricGenerator {

    private static final Random random = new Random(123); // Fixed seed for reproducibility

    public static List<FetusMetric> generateSampleFetusMetrics(Fetus fetus, List<Metric> metrics) {
        List<FetusMetric> fetusMetrics = new ArrayList<>();

        // Generate metrics for a healthy pregnancy progression
        // Creating a complete set of measurements for weeks 4 through 40
        for (int week = 4; week <= fetus.getCurrentWeek(); week++) {
            for (Metric metric : metrics) {
                // Only generate data for metrics applicable to the current week
                if (isMetricApplicableForWeek(metric.getName(), week)) {
                    FetusMetric fetusMetric = new FetusMetric();
                    fetusMetric.setFetus(fetus);
                    fetusMetric.setMetric(metric);
                    fetusMetric.setWeek(week);
                    
                    // Generate a value within the standard range for this week
                    double value = generateValueForMetricAndWeek(metric, week);
                    fetusMetric.setValue(value);
                    
                    fetusMetrics.add(fetusMetric);
                }
            }
        }
        
        return fetusMetrics;
    }
    
    private static boolean isMetricApplicableForWeek(String metricName, int week) {
        switch (metricName) {
            case "Gestational Sac Diameter":
                return week >= 4 && week <= 12;
            case "Yolk Sac Diameter":
                return week >= 5 && week <= 12;
            case "Beta-hCG":
                return week >= 1 && week <= 6;
            case "Mean Sac Diameter":
                return week >= 4 && week <= 10;
            case "Biparietal Diameter":
                return week >= 13;
            case "Crown-Rump Length":
                return week >= 6 && week <= 14;
            case "Abdominal Circumference", "Femur Length":
                return week >= 14;
            case "Fetal Heart Rate":
                return week >= 6;
            case "Weight":
                return week >= 20;
            default:
                return false;
        }
    }
    
    private static double generateValueForMetricAndWeek(Metric metric, int week) {
        // Find the appropriate standard for this week
        for (var standard : metric.getStandards()) {
            if (standard.getWeek() == week) {
                // Generate a value within the normal range
                // Slightly biased towards the center of the range (healthier fetus)
                double min = standard.getMin();
                double max = standard.getMax();
                double range = max - min;
                
                // Generate a value with normal distribution around the center
                double center = min + (range / 2);
                double stdDev = range / 6; // So that 99.7% of values fall within the range
                double value = random.nextGaussian() * stdDev + center;
                
                // Ensure the value stays within the min-max range
                return Math.max(min, Math.min(max, value));
            }
        }
        
        // If we don't have a standard for this week, interpolate from the closest weeks
        return interpolateValue(metric, week);
    }
    
    private static double interpolateValue(Metric metric, int week) {
        int lowerWeek = -1;
        int upperWeek = -1;
        double lowerValue = 0;
        double upperValue = 0;
        
        // Find the closest weeks with standards
        for (var standard : metric.getStandards()) {
            int standardWeek = standard.getWeek();
            
            if (standardWeek < week && (lowerWeek == -1 || standardWeek > lowerWeek)) {
                lowerWeek = standardWeek;
                lowerValue = (standard.getMin() + standard.getMax()) / 2;
            }
            
            if (standardWeek > week && (upperWeek == -1 || standardWeek < upperWeek)) {
                upperWeek = standardWeek;
                upperValue = (standard.getMin() + standard.getMax()) / 2;
            }
        }
        
        // If we have both lower and upper bounds, interpolate
        if (lowerWeek != -1 && upperWeek != -1) {
            double ratio = (double)(week - lowerWeek) / (upperWeek - lowerWeek);
            return lowerValue + ratio * (upperValue - lowerValue);
        } 
        // If we only have lower bound, extrapolate forward
        else if (lowerWeek != -1) {
            // Find the rate of change from previous weeks
            double prevValue = 0;
            for (var standard : metric.getStandards()) {
                if (standard.getWeek() == lowerWeek - 1) {
                    prevValue = (standard.getMin() + standard.getMax()) / 2;
                    break;
                }
            }
            
            // If we found a previous value, calculate rate of change
            if (prevValue > 0) {
                double weeklyChange = lowerValue - prevValue;
                return lowerValue + weeklyChange * (week - lowerWeek);
            } else {
                return lowerValue; // Just use the nearest value
            }
        } 
        // If we only have upper bound, extrapolate backward
        else if (upperWeek != -1) {
            // Find the rate of change from next weeks
            double nextValue = 0;
            for (var standard : metric.getStandards()) {
                if (standard.getWeek() == upperWeek + 1) {
                    nextValue = (standard.getMin() + standard.getMax()) / 2;
                    break;
                }
            }
            
            // If we found a next value, calculate rate of change
            if (nextValue > 0) {
                double weeklyChange = nextValue - upperValue;
                return upperValue - weeklyChange * (upperWeek - week);
            } else {
                return upperValue; // Just use the nearest value
            }
        }
        
        // If we have no nearby standards, return a default value
        return 0.0;
    }
    
    // Main method to demonstrate usage
    public static void main(String[] args) {
        // Create a sample fetus
        Fetus fetus = new Fetus();
        fetus.setId(1L);
        fetus.setNickName("Baby Smith");
        
        // Generate metrics for the fetus
        List<FetusMetric> fetusMetrics = generateSampleFetusMetrics(fetus, MetricGenerator.generateSampleMetrics());
        
        // Display generated metrics
        System.out.println("Generated " + fetusMetrics.size() + " metrics for " + fetus.getNickName());
        
        // Group metrics by week for display
        for (int week = 4; week <= 40; week++) {
            System.out.println("\nWeek " + week + " measurements:");
            boolean hasMetrics = false;
            
            for (FetusMetric metric : fetusMetrics) {
                if (metric.getWeek() == week) {
                    hasMetrics = true;
                    System.out.printf("  %s: %.2f %s%n", 
                                    metric.getMetric().getName(), 
                                    metric.getValue(), 
                                    metric.getMetric().getUnit());
                }
            }
            
            if (!hasMetrics) {
                System.out.println("  No measurements recorded");
            }
        }
    }
}