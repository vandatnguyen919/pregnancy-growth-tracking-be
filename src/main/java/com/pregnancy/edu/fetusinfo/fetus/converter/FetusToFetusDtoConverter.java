package com.pregnancy.edu.fetusinfo.fetus.converter;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FetusToFetusDtoConverter implements Converter<Fetus, FetusDto> {
    @Override
    public FetusDto convert(Fetus source) {
        return new FetusDto(
                source.getId(),
                source.getUser().getId(),
                source.getPregnancy().getId(),
                source.getNickName(),
                source.getGender(),
                source.getFetusNumber(),
                source.getFetusMetrics() == null ? 0 : source.getFetusMetrics().size()
        );
    }
}
