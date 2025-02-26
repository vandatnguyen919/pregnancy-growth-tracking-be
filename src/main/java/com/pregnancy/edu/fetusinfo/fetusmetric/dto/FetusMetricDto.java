package com.pregnancy.edu.fetusinfo.fetusmetric.dto;

import jakarta.validation.constraints.NotNull;

public record FetusMetricDto(
        Long id,
        @NotNull(message = "Fetus ID is required")
        Long fetusId,
        @NotNull(message = "Metric ID is required")
        Long metricId,
        @NotNull(message = "Value is required")
        Double value,
        @NotNull(message = "Week is required")
        Integer week
) {
}