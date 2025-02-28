package com.pregnancy.edu.fetusinfo.metric.converter;

import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MetricToDtoConverter implements Converter<Metric, MetricDto> {
    @Override
    public MetricDto convert(Metric source) {
        return new MetricDto(
                source.getId(),
                source.getName(),
                source.getUnit()
        );
    }
}
