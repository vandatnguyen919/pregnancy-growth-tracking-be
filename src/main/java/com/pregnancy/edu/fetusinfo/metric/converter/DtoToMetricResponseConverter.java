package com.pregnancy.edu.fetusinfo.metric.converter;

import com.pregnancy.edu.fetusinfo.metric.dto.MetricDto;
import com.pregnancy.edu.fetusinfo.metric.dto.MetricResponse;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DtoToMetricResponseConverter implements Converter<MetricDto, MetricResponse> {
    private final StandardRepository standardRepository;

    public DtoToMetricResponseConverter(StandardRepository standardRepository) {
        this.standardRepository = standardRepository;
    }

    @Override
    public MetricResponse convert(MetricDto source) {
        String metricName = source.name();

        Optional<Standard> standard = standardRepository.findByMetricIdAndWeek(source.id(), null);

        Double min = standard.map(Standard::getMin).orElse(null);
        Double max = standard.map(Standard::getMax).orElse(null);

        return new MetricResponse(
                metricName,
                source.unit(),
                min,
                max
        );
    }

    public MetricResponse convert(MetricDto source, Integer week) {
        String metricName = source.name();

        Optional<Standard> standard = standardRepository.findByMetricIdAndWeek(source.id(), week);

        Double min = standard.map(Standard::getMin).orElse(null);
        Double max = standard.map(Standard::getMax).orElse(null);

        return new MetricResponse(
                metricName,
                source.unit(),
                min,
                max
        );
    }
}
