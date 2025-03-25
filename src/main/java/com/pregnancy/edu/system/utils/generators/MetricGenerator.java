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
        int[] weeks = {13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        double[] mins = {24.0, 27.5, 29.8, 32.0, 35.0, 38.0, 41.0, 44.0, 47.0, 50.0, 54.0, 58.0, 61.5, 65.0, 68.5, 72.0, 74.5, 77.0, 79.0, 81.0, 83.0, 85.0, 86.5, 88.0, 89.0, 90.0, 91.0, 92.0};
        double[] maxs = {31.0, 34.5, 37.3, 40.0, 43.0, 46.0, 49.0, 52.0, 55.0, 58.0, 62.0, 66.0, 69.5, 73.0, 76.5, 80.0, 82.5, 85.0, 87.0, 89.0, 91.0, 93.0, 94.5, 96.0, 97.0, 98.0, 99.0, 100.0};

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
        int[] weeks = {14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        double[] mins = {75.0, 85.0, 95.0, 105.0, 115.0, 125.0, 135.0, 147.5, 160.0, 172.5, 185.0, 195.0, 205.0, 215.0, 225.0, 235.0, 245.0, 255.0, 265.0, 275.0, 285.0, 292.5, 300.0, 307.5, 315.0, 322.5, 330.0};
        double[] maxs = {95.0, 105.0, 115.0, 125.0, 135.0, 145.0, 155.0, 167.5, 180.0, 192.5, 205.0, 215.0, 225.0, 235.0, 245.0, 255.0, 265.0, 275.0, 285.0, 295.0, 305.0, 312.5, 320.0, 327.5, 335.0, 342.5, 350.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> createFHRStandards(Metric metric) {

        // FHR standards by week (detectable from week 6)
        int[] weeks = {6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        double[] mins = {90.0, 100.0, 110.0, 120.0, 130.0, 135.0, 140.0, 140.0, 140.0, 140.0, 140.0, 130.0, 130.0, 125.0, 120.0, 120.0, 120.0, 120.0, 120.0, 115.0, 115.0, 115.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0, 110.0};
        double[] maxs = {110.0, 120.0, 130.0, 160.0, 170.0, 170.0, 170.0, 165.0, 170.0, 165.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 155.0, 155.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> createWeightStandards(Metric metric) {

        // Weight standards by week (not typically estimated before week 20)
        int[] weeks = {20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        double[] mins = {250.0, 300.0, 350.0, 425.0, 500.0, 575.0, 650.0, 750.0, 850.0, 950.0, 1050.0, 1175.0, 1300.0, 1550.0, 1800.0, 2050.0, 2300.0, 2500.0, 2700.0, 2850.0, 3000.0};
        double[] maxs = {350.0, 425.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1050.0, 1200.0, 1350.0, 1500.0, 1700.0, 1900.0, 2200.0, 2500.0, 2750.0, 3000.0, 3250.0, 3500.0, 3750.0, 4000.0};

        return getStandards(metric, weeks, mins, maxs);
    }

    private static List<Standard> createFLStandards(Metric metric) {

        // FL standards by week (measured from week 14 onwards)
        int[] weeks = {14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};
        double[] mins = {10.0, 12.5, 15.0, 17.5, 20.0, 22.5, 25.0, 27.5, 30.0, 32.5, 35.0, 37.5, 40.0, 42.5, 45.0, 47.5, 50.0, 52.5, 55.0, 57.5, 60.0, 62.5, 65.0, 67.5, 70.0, 71.0, 72.0};
        double[] maxs = {16.0, 18.5, 21.0, 23.5, 26.0, 28.5, 31.0, 33.5, 36.0, 38.5, 41.0, 43.5, 46.0, 48.5, 51.0, 53.5, 56.0, 58.5, 61.0, 63.5, 66.0, 68.5, 71.0, 73.5, 76.0, 77.0, 78.0};

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