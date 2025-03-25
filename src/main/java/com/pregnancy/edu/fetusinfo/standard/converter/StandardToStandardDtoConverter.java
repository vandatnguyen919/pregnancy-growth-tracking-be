package com.pregnancy.edu.fetusinfo.standard.converter;

import com.pregnancy.edu.fetusinfo.metric.converter.MetricToMetricDtoConverter;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.dto.StandardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StandardToStandardDtoConverter implements Converter<Standard, StandardDto> {

    private final MetricToMetricDtoConverter metricToMetricDtoConverter;

    public StandardToStandardDtoConverter(MetricToMetricDtoConverter metricToMetricDtoConverter) {
        this.metricToMetricDtoConverter = metricToMetricDtoConverter;
    }

    @Override
    public StandardDto convert(Standard source) {
        return new StandardDto(
                source.getId(),
                source.getMetric().getId(),
                metricToMetricDtoConverter.convert(source.getMetric()),
                source.getWeek(),
                source.getMin(),
                source.getMax()
        );
    }
}