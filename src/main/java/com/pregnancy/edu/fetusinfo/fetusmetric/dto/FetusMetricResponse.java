package com.pregnancy.edu.fetusinfo.fetusmetric.dto;

public record FetusMetricResponse(
        Long fetusId,
        String metricName,
        Double value
) {
}
