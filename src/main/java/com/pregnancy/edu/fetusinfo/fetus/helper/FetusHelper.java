package com.pregnancy.edu.fetusinfo.fetus.helper;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusWeekMetricsResponse;
import com.pregnancy.edu.fetusinfo.fetus.dto.MetricWithValueDto;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.standard.Standard;

import java.util.ArrayList;
import java.util.List;

public class FetusHelper {
    public FetusWeekMetricsResponse buildFetusWeekResponse(Fetus fetus, List<Metric> metrics, Integer week) {
        List<MetricWithValueDto> metricWithValues = new ArrayList<>();

        for (Metric metric : metrics) {
            Standard standard = metric.getStandards().stream()
                    .filter(s -> s.getWeek().equals(week))
                    .findFirst()
                    .orElse(null);

            Double min = standard != null ? standard.getMin() : null;
            Double max = standard != null ? standard.getMax() : null;

            // Check if there is a recorded value for this fetus, metric, and week
            FetusMetric fetusMetric = fetus.getFetusMetrics().stream()
                    .filter(fm -> fm.getMetric().getId().equals(metric.getId()) &&
                            fm.getWeek().equals(week))
                    .findFirst()
                    .orElse(null);

            Double value = fetusMetric != null ? fetusMetric.getValue() : null;

            MetricWithValueDto metricDto = new MetricWithValueDto(
                    metric.getId(),
                    metric.getName(),
                    metric.getDataType(),
                    metric.getUnit(),
                    min,
                    max,
                    value
            );

            metricWithValues.add(metricDto);
        }

        return new FetusWeekMetricsResponse(
                fetus.getId(),
                fetus.getNickName(),
                fetus.getGender(),
                fetus.getFetusNumber(),
                week,
                metricWithValues
        );
    }
}


