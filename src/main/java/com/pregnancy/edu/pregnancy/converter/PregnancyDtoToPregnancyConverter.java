package com.pregnancy.edu.pregnancy.converter;

import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.dto.PregnancyDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PregnancyDtoToPregnancyConverter implements Converter<PregnancyDto, Pregnancy> {

    public PregnancyDtoToPregnancyConverter() {
    }

    @Override
    public Pregnancy convert(PregnancyDto source) {
        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setEstimatedDueDate(source.dueDate());
        pregnancy.setPregnancyStartDate(source.dueDate().minusDays(266));
        pregnancy.setDeliveryDate(source.dueDate().plusDays(21));

        return pregnancy;
    }
}