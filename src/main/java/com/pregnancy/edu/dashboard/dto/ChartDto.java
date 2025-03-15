package com.pregnancy.edu.dashboard.dto;

import java.util.List;

public record ChartDto(
        List<ChartDataItem> data
) {
}
