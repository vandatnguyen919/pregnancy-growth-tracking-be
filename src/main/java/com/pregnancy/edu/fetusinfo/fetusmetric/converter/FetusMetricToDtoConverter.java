package com.pregnancy.edu.fetusinfo.fetusmetric.converter;

import com.pregnancy.edu.fetusinfo.fetusmetric.FetusMetric;
import com.pregnancy.edu.fetusinfo.fetusmetric.dto.FetusMetricDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FetusMetricToDtoConverter implements Converter<FetusMetric, FetusMetricDto> {
    @Override
    public FetusMetricDto convert(FetusMetric source) {
        return new FetusMetricDto(
                source.getId(),
                source.getFetus().getId(),
                source.getMetric().getId(),
                source.getValue(),
                source.getWeek()
        );
    }
}