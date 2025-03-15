package com.pregnancy.edu.dashboard.dto;

import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;

import java.util.List;

public record LineDataDto(
        MetricDto metric,
        List<LineDataItem> data
) {
}
