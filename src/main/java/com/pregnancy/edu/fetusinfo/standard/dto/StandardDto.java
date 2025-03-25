package com.pregnancy.edu.fetusinfo.standard.dto;

import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;
import jakarta.validation.constraints.NotNull;

public record StandardDto(
        Long id,
        @NotNull(message = "Metric ID is required")
        Long metricId,
        MetricDto metric,
        @NotNull(message = "Week is required")
        Integer week,
        @NotNull(message = "Minimum value is required")
        Double min,
        @NotNull(message = "Maximum value is required")
        Double max
) {
}