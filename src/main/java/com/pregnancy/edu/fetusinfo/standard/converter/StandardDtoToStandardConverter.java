package com.pregnancy.edu.fetusinfo.standard.converter;

import com.pregnancy.edu.fetusinfo.standard.Standard;
import com.pregnancy.edu.fetusinfo.standard.dto.StandardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StandardDtoToStandardConverter implements Converter<StandardDto, Standard> {

    public StandardDtoToStandardConverter() {
    }

    @Override
    public Standard convert(StandardDto source) {
        Standard standard = new Standard();
        standard.setWeek(source.week());
        standard.setMin(source.min());
        standard.setMax(source.max());

        return standard;
    }
}