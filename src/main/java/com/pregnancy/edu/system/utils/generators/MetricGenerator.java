package com.pregnancy.edu.system.utils.generators;

import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.standard.Standard;

import java.util.ArrayList;
import java.util.List;

public class MetricGenerator {

    public static List<Metric> generateSampleMetrics() {
        List<Metric> metrics = new ArrayList<>();

        // Gestational Sac (measurable from weeks 4-12)
        Metric gestationalSac = new Metric();
        gestationalSac.setName("Gestational Sac Diameter");
        gestationalSac.setDataType("NUMERIC");
        gestationalSac.setUnit("mm");
        gestationalSac.setStandards(createGestationalSacStandards(gestationalSac));
        metrics.add(gestationalSac);

        // Yolk Sac (measurable from weeks 5-12)
        Metric yolkSac = new Metric();
        yolkSac.setName("Yolk Sac Diameter");
        yolkSac.setDataType("NUMERIC");
        yolkSac.setUnit("mm");
        yolkSac.setStandards(createYolkSacStandards(yolkSac));
        metrics.add(yolkSac);

        // Beta-hCG levels (measurable from weeks 1-6)
        Metric hcg = new Metric();
        hcg.setName("Beta-hCG");
        hcg.setDataType("NUMERIC");
        hcg.setUnit("mIU/mL");
        hcg.setStandards(createHCGStandards(hcg));
        metrics.add(hcg);

        // MSD - Mean Sac Diameter (measurable from weeks 4-12)
        Metric msd = new Metric();
        msd.setName("Mean Sac Diameter");
        msd.setDataType("NUMERIC");
        msd.setUnit("mm");
        msd.setStandards(createMSDStandards(msd));
        metrics.add(msd);
        
        // BPD - Biparietal Diameter (measured from week 13 onwards)
        Metric bpd = new Metric();
        bpd.setName("Biparietal Diameter");
        bpd.setDataType("NUMERIC");
        bpd.setUnit("mm");
        bpd.setStandards(createBPDStandards(bpd));
        metrics.add(bpd);
        
        // CRL - Crown-Rump Length (measured until week 14)
        Metric crl = new Metric();
        crl.setName("Crown-Rump Length");
        crl.setDataType("NUMERIC");
        crl.setUnit("mm");
        crl.setStandards(createCRLStandards(crl));
        metrics.add(crl);
        
        // AC - Abdominal Circumference (measured from week 14 onwards)
        Metric ac = new Metric();
        ac.setName("Abdominal Circumference");
        ac.setDataType("NUMERIC");
        ac.setUnit("mm");
        ac.setStandards(createACStandards(ac));
        metrics.add(ac);
        
        // FHR - Fetal Heart Rate
        Metric fhr = new Metric();
        fhr.setName("Fetal Heart Rate");
        fhr.setDataType("NUMERIC");
        fhr.setUnit("bpm");
        fhr.setStandards(createFHRStandards(fhr));
        metrics.add(fhr);
        
        // Weight (estimated from week 20 onwards)
        Metric weight = new Metric();
        weight.setName("Weight");
        weight.setDataType("NUMERIC");
        weight.setUnit("g");
        weight.setStandards(createWeightStandards(weight));
        metrics.add(weight);
        
        // FL - Femur Length (measured from week 14 onwards)
        Metric fl = new Metric();
        fl.setName("Femur Length");
        fl.setDataType("NUMERIC");
        fl.setUnit("mm");
        fl.setStandards(createFLStandards(fl));
        metrics.add(fl);
        
        return metrics;
    }

    private static List<Standard> createGestationalSacStandards(Metric metric) {

        // Gestational sac standards by week
        int[] weeks = {4, 5, 6, 7, 8, 9, 10, 11, 12};
        double[] mins = {2.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0};
        double[] maxs = {6.0, 12.0, 18.0, 24.0, 30.0, 35.0, 40.0, 45.0, 50.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> createYolkSacStandards(Metric metric) {

        // Yolk sac standards by week (appears around week 5)
        int[] weeks = {5, 6, 7, 8, 9, 10, 11, 12};
        double[] mins = {2.0, 3.0, 3.0, 3.0, 3.0, 3.0, 2.0, 2.0};
        double[] maxs = {5.0, 5.5, 5.5, 5.5, 5.5, 5.5, 5.0, 5.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> createHCGStandards(Metric metric) {

        // Beta-hCG standards by week
        int[] weeks = {1, 2, 3, 4, 5, 6};
        double[] mins = {5.0, 50.0, 500.0, 5000.0, 10000.0, 15000.0};
        double[] maxs = {50.0, 500.0, 5000.0, 100000.0, 100000.0, 200000.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> createMSDStandards(Metric metric) {

        // Mean Sac Diameter standards by week
        int[] weeks = {4, 5, 6, 7, 8, 9, 10};
        double[] mins = {2.0, 6.0, 12.0, 18.0, 24.0, 30.0, 36.0};
        double[] maxs = {8.0, 14.0, 20.0, 26.0, 32.0, 38.0, 44.0};

        return getStandards(metric, weeks, mins, maxs);
    }
    
    private static List<Standard> createBPDStandards(Metric metric) {

        // BPD standards by week (not measurable before week 13)
        int[] weeks = {13, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
        double[] mins = {24.0, 27.5, 32.0, 38.0, 44.0, 50.0, 58.0, 65.0, 72.0, 77.0, 81.0, 85.0, 88.0, 90.0, 92.0};
        double[] maxs = {31.0, 34.5, 40.0, 46.0, 52.0, 58.0, 66.0, 73.0, 80.0, 85.0, 89.0, 93.0, 96.0, 98.0, 100.0};

        return getStandards(metric, weeks, mins, maxs);
    }
    
    private static List<Standard> createCRLStandards(Metric metric) {

        // CRL standards by week (typically only measured until week 14)
        int[] weeks = {6, 7, 8, 9, 10, 11, 12, 13, 14};
        double[] mins = {2.0, 5.0, 10.0, 17.0, 23.0, 32.0, 42.0, 54.0, 65.0};
        double[] maxs = {8.0, 13.0, 20.0, 28.0, 38.0, 48.0, 59.0, 72.0, 85.0};

        return getStandards(metric, weeks, mins, maxs);
    }
    
    private static List<Standard> createACStandards(Metric metric) {

        // AC standards by week (not typically measured before week 14)
        int[] weeks = {14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
        double[] mins = {75.0, 95.0, 115.0, 135.0, 160.0, 185.0, 205.0, 225.0, 245.0, 265.0, 285.0, 300.0, 315.0, 330.0};
        double[] maxs = {95.0, 115.0, 135.0, 155.0, 180.0, 205.0, 225.0, 245.0, 265.0, 285.0, 305.0, 320.0, 335.0, 350.0};

        return getStandards(metric, weeks, mins, maxs);
    }
    
    private static List<Standard> createFHRStandards(Metric metric) {

        // FHR standards by week (detectable from week 6)
        int[] weeks = {6, 7, 8, 9, 10, 12, 14, 16, 20, 24, 28, 32, 36, 40};
        double[] mins = {90.0, 100.0, 110.0, 120.0, 130.0, 140.0, 140.0, 140.0, 120.0, 120.0, 110.0, 110.0, 110.0, 110.0};
        double[] maxs = {110.0, 120.0, 130.0, 160.0, 170.0, 170.0, 170.0, 160.0, 160.0, 160.0, 150.0, 150.0, 150.0, 150.0};

        return getStandards(metric, weeks, mins, maxs);
    }
    
    private static List<Standard> createWeightStandards(Metric metric) {

        // Weight standards by week (not typically estimated before week 20)
        int[] weeks = {20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
        double[] mins = {250.0, 350.0, 500.0, 650.0, 850.0, 1050.0, 1300.0, 1800.0, 2300.0, 2700.0, 3000.0};
        double[] maxs = {350.0, 500.0, 700.0, 900.0, 1200.0, 1500.0, 1900.0, 2500.0, 3000.0, 3500.0, 4000.0};

        return getStandards(metric, weeks, mins, maxs);
    }
    
    private static List<Standard> createFLStandards(Metric metric) {

        // FL standards by week (not typically measured before week 14)
        int[] weeks = {14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
        double[] mins = {10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0, 55.0, 60.0, 65.0, 70.0, 72.0};
        double[] maxs = {16.0, 21.0, 26.0, 31.0, 36.0, 41.0, 46.0, 51.0, 56.0, 61.0, 66.0, 71.0, 76.0, 78.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> getStandards(Metric metric, int[] weeks, double[] mins, double[] maxs) {
        assert weeks.length == mins.length;
        assert weeks.length == maxs.length;

        List<Standard> standards = new ArrayList<>(weeks.length);
        for (int i = 0; i < weeks.length; i++) {
            Standard standard = new Standard();
            standard.setMetric(metric);
            standard.setWeek(weeks[i]);
            standard.setMin(mins[i]);
            standard.setMax(maxs[i]);
            standards.add(standard);
        }
        return standards;
    }

    // Main method to demonstrate usage
    public static void main(String[] args) {
        List<Metric> metrics = generateSampleMetrics();
        
        // Display generated metrics and their standards
        for (Metric metric : metrics) {
            System.out.println("Metric: " + metric.getName() + " (" + metric.getUnit() + ")");
            
            for (Standard standard : metric.getStandards()) {
                System.out.println("  Week " + standard.getWeek() + 
                                   ": " + standard.getMin() + 
                                   " - " + standard.getMax() + 
                                   " " + metric.getUnit());
            }
            System.out.println();
        }
    }
}