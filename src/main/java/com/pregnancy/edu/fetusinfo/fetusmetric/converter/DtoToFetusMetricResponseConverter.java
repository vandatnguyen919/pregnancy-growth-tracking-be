package com.pregnancy.edu.fetusinfo.fetusmetric.converter;

import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricResponse;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.StandardRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DtoToFetusMetricResponseConverter implements Converter<FetusMetricDto, FetusMetricResponse> {
    private final MetricRepository metricRepository;
    private final StandardRepository standardRepository;

    public DtoToFetusMetricResponseConverter(MetricRepository metricRepository, StandardRepository standardRepository) {
        this.metricRepository = metricRepository;
        this.standardRepository = standardRepository;
    }

    @Override
    public FetusMetricResponse convert(FetusMetricDto source) {
        String metricName = metricRepository.findById(source.metricId())
                .map(metric -> metric.getName())
                .orElse("Unknown Metric");

        Optional<Standard> standard = standardRepository.findByMetricIdAndWeek(source.metricId(), null);

        return new FetusMetricResponse(
                source.fetusId(),
                metricName,
                source.value()
        );
    }

    public FetusMetricResponse convert(FetusMetricDto source, Integer week) {
        String metricName = metricRepository.findById(source.metricId())
                .map(metric -> metric.getName())
                .orElse("Unknown Metric");

        Optional<Standard> standard = standardRepository.findByMetricIdAndWeek(source.metricId(), week);

        return new FetusMetricResponse(
                source.fetusId(),
                metricName,
                source.value()
        );
    }

}
