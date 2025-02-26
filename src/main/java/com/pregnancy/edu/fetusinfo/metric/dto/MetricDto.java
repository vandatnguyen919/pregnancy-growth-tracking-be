package com.pregnancy.edu.fetusinfo.metric.dto;

import jakarta.validation.constraints.NotEmpty;

public record MetricDto(
        Long id,
        @NotEmpty(message = "Name is required")
        String name,
        String dataType,
        String unit,
        Integer standardsCount,
        Integer metricsCount
) {
}