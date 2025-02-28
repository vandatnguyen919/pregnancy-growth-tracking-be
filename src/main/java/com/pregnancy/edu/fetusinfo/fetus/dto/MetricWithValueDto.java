package com.pregnancy.edu.fetusinfo.fetus.dto;

public record MetricWithValueDto(
        Long id,
        String name,
        String dataType,
        String unit,
        Double min,
        Double max,
        Double value
) {
}
