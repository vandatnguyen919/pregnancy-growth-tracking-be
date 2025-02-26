package com.pregnancy.edu.fetusinfo.fetusmetric.converter;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusRepository;
import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import com.pregnancy.edu.fetusinfo.metric.Metric;
import com.pregnancy.edu.fetusinfo.metric.MetricRepository;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToFetusMetricConverter implements Converter<FetusMetricDto, FetusMetric> {

    private final FetusRepository fetusRepository;
    private final MetricRepository metricRepository;

    public DtoToFetusMetricConverter(FetusRepository fetusRepository, MetricRepository metricRepository) {
        this.fetusRepository = fetusRepository;
        this.metricRepository = metricRepository;
    }

    @Override
    public FetusMetric convert(FetusMetricDto source) {
        FetusMetric fetusMetric = new FetusMetric();

        Fetus fetus = fetusRepository.findById(source.fetusId())
                .orElseThrow(() -> new ObjectNotFoundException("fetus", source.fetusId()));

        Metric metric = metricRepository.findById(source.metricId())
                .orElseThrow(() -> new ObjectNotFoundException("metric", source.metricId()));

        fetusMetric.setFetus(fetus);
        fetusMetric.setMetric(metric);
        fetusMetric.setValue(source.value());
        fetusMetric.setWeek(source.week());

        return fetusMetric;
    }
}