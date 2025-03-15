package com.pregnancy.edu.fetusinfo.metric.converter;

import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MetricDtoToMetricConverter implements Converter<MetricDto, Metric> {
    @Override
    public Metric convert(MetricDto source) {
        Metric metric = new Metric();
        metric.setName(source.name());
        metric.setUnit(source.unit());
        return metric;
    }
}