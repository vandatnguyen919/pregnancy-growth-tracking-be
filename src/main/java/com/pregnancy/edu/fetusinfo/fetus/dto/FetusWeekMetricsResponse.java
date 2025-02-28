package com.pregnancy.edu.fetusinfo.fetus.dto;

import java.util.List;

public record FetusWeekMetricsResponse(
        Long fetusId,
        String nickName,
        String gender,
        Integer fetusNumber,
        Integer week,
        List<MetricWithValueDto> metrics
) {
}
