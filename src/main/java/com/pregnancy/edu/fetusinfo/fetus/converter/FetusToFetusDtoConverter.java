package com.pregnancy.edu.fetusinfo.fetus.converter;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FetusToFetusDtoConverter implements Converter<Fetus, FetusDto> {
    @Override
    public FetusDto convert(Fetus source) {
        Long userId = source.getUser() != null ? source.getUser().getId() : null;

        return new FetusDto(
                source.getId(),
                userId,
                source.getPregnancy().getId(),
                source.getNickName(),
                source.getGender()
        );
    }
}
