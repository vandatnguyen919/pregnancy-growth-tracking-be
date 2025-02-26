package com.pregnancy.edu.fetusinfo.fetus.converter;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.dto.FetusDto;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.PregnancyRepository;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FetusDtoToFetusConverter implements Converter<FetusDto, Fetus> {

    private final UserRepository userRepository;
    private final PregnancyRepository pregnancyRepository;

    public FetusDtoToFetusConverter(UserRepository userRepository, PregnancyRepository pregnancyRepository) {
        this.userRepository = userRepository;
        this.pregnancyRepository = pregnancyRepository;
    }

    @Override
    public Fetus convert(FetusDto source) {
        Fetus fetus = new Fetus();

        MyUser user = userRepository.findById(source.userId())
                .orElseThrow(() -> new ObjectNotFoundException("user", source.userId()));

        Pregnancy pregnancy = pregnancyRepository.findById(source.pregnancyId())
                .orElseThrow(() -> new ObjectNotFoundException("pregnancy", source.pregnancyId()));

        fetus.setUser(user);
        fetus.setPregnancy(pregnancy);
        fetus.setNickName(source.nickName());
        fetus.setGender(source.gender());
        fetus.setFetusNumber(source.fetusNumber());

        return fetus;
    }
}