package com.pregnancy.edu.fetusinfo.standard.converter;

import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.dto.StandardDto;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StandardDtoToStandardConverter implements Converter<StandardDto, Standard> {

    private final MetricRepository metricRepository;

    public StandardDtoToStandardConverter(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Override
    public Standard convert(StandardDto source) {
        Standard standard = new Standard();

        Metric metric = metricRepository.findById(source.metricId())
                .orElseThrow(() -> new ObjectNotFoundException("metric", source.metricId()));

        standard.setMetric(metric);
        standard.setWeek(source.week());
        standard.setMin(source.min());
        standard.setMax(source.max());

        return standard;
    }
}