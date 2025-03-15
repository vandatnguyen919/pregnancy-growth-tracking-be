package com.pregnancy.edu.dashboard.dto;

import java.util.List;

public record RadarDataDto(
        Double max,
        List<RadarDataItem> data
) {
}
