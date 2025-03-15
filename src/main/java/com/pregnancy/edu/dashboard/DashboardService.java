package com.pregnancy.edu.dashboard;

import com.pregnancy.edu.dashboard.dto.RadarDataItem;
import com.pregnancy.edu.dashboard.dto.RadarDataDto;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetricService;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final FetusMetricService fetusMetricService;
    private final StandardService standardService;

    private final String MAX = "max";
    private final String MIN = "min";
    private final String CURRENT = "cur";

    public DashboardService(FetusMetricService fetusMetricService, StandardService standardService) {
        this.fetusMetricService = fetusMetricService;
        this.standardService = standardService;
    }

    public RadarDataDto getRadarData(Long fetusId, Integer week) {
        List<FetusMetric> fetusMetrics = fetusMetricService.findByFetusIdAndWeek(fetusId, week);
        if (fetusMetrics.size() < 3) {
            throw new IllegalArgumentException("Fetus metrics are not enough to generate radar data");
        }
        double max = Double.MIN_VALUE;
        List<RadarDataItem> data = new ArrayList<>();
        for (FetusMetric fetusMetric : fetusMetrics) {
            Metric metric = fetusMetric.getMetric();
            Standard standard = standardService.findByMetricIdAndWeek(metric.getId(), fetusMetric.getWeek());
            max = Double.max(max, Double.max(standard.getMax(), fetusMetric.getValue()));

            // Round the values to 2 decimal places
            double roundedMax = Math.round(standard.getMax() * 100.0) / 100.0;
            double roundedCurrent = Math.round(fetusMetric.getValue() * 100.0) / 100.0;
            double roundedMin = Math.round(standard.getMin() * 100.0) / 100.0;

            data.add(new RadarDataItem(metric.getName(), MAX, roundedMax));
            data.add(new RadarDataItem(metric.getName(), CURRENT, roundedCurrent));
            data.add(new RadarDataItem(metric.getName(), MIN, roundedMin));
        }
        // Round the final max value as well
        double roundedMax = Math.round(max * 100.0) / 100.0;
        return new RadarDataDto(roundedMax, data);
    }
}
