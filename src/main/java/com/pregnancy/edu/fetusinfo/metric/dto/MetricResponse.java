package com.pregnancy.edu.fetusinfo.metric.dto;

public record MetricResponse(
        String metricName,
        String unit,
        Double min,
        Double max
) {
}