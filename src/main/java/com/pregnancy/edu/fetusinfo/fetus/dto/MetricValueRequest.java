package com.pregnancy.edu.fetusinfo.fetus.dto;

public record MetricValueRequest(
        Long metricId,
        Double value
) {
}
