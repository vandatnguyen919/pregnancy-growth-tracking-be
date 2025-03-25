package com.pregnancy.edu.fetusinfo.fetusmetric.converter;

import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DtoToFetusMetricConverter implements Converter<FetusMetricDto, FetusMetric> {

    @Override
    public FetusMetric convert(FetusMetricDto source) {
        FetusMetric fetusMetric = new FetusMetric();
        fetusMetric.setValue(source.value());

        return fetusMetric;
    }
}