package com.pregnancy.edu.fetusinfo.metric.dto;

public record MetricResponse(
        Long metricId,
        String metricName,
        String unit,
        Double min,
        Double max
) {
}