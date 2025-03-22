package com.pregnancy.edu.fetusinfo.fetus.converter;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FetusDtoToFetusConverter implements Converter<FetusDto, Fetus> {

    public FetusDtoToFetusConverter() {
    }

    @Override
    public Fetus convert(FetusDto source) {
        Fetus fetus = new Fetus();
        fetus.setNickName(source.nickName());
        fetus.setGender(source.gender());
        return fetus;
    }
}