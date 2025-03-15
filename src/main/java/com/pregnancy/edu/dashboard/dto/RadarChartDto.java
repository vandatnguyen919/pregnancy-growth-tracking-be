package com.pregnancy.edu.dashboard.dto;

import java.util.List;

public record RadarChartDto(
        Double max,
        List<ChartDataItem> data
) {
}
