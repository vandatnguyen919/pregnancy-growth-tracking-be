package com.pregnancy.edu.pregnancy.converter;

import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.dto.PregnancyDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PregnancyToPregnancyDtoConverter implements Converter<Pregnancy, PregnancyDto> {

    @Override
    public PregnancyDto convert(Pregnancy source) {
        return new PregnancyDto(
                source.getId(),
                source.getUser().getId(),
                source.getMaternalAge(),
                source.getPregnancyStartDate(),
                source.getEstimatedDueDate(),
                source.getDeliveryDate(),
                source.getStatus(),
                source.getFetuses().size()
        );
    }
}