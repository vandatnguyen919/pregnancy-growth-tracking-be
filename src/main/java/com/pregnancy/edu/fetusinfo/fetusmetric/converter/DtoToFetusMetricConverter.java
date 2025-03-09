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


    public DtoToFetusMetricConverter() {
    }

    @Override
    public FetusMetric convert(FetusMetricDto source) {
        FetusMetric fetusMetric = new FetusMetric();
        fetusMetric.setValue(source.value());
        fetusMetric.setWeek(source.week());

        return fetusMetric;
    }
}