package com.pregnancy.edu.pregnancy.converter;

import com.pregnancy.edu.fetusinfo.fetus.Fetus;
import com.pregnancy.edu.fetusinfo.fetus.FetusRepository;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.pregnancy.Pregnancy;
import com.pregnancy.edu.pregnancy.dto.PregnancyDto;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PregnancyDtoToPregnancyConverter implements Converter<PregnancyDto, Pregnancy> {

    private final UserRepository userRepository;
    private final FetusRepository fetusRepository;

    public PregnancyDtoToPregnancyConverter(UserRepository userRepository,
                                            FetusRepository fetusRepository) {
        this.userRepository = userRepository;
        this.fetusRepository = fetusRepository;
    }

    @Override
    public Pregnancy convert(PregnancyDto source) {
        Pregnancy pregnancy = new Pregnancy();

        // Find the user
        MyUser user = userRepository.findById(source.userId())
                .orElseThrow(() -> new ObjectNotFoundException("user", source.userId()));

        // Set basic properties
        pregnancy.setUser(user);
        pregnancy.setMaternalAge(source.maternalAge());
        pregnancy.setPregnancyStartDate(source.pregnancyStartDate());
        pregnancy.setEstimatedDueDate(source.estimatedDueDate());
        pregnancy.setDeliveryDate(source.deliveryDate());
        pregnancy.setStatus(source.status());

        // Handle fetuses if they exist and were included in the DTO
        if (source.fetusIds() != null && !source.fetusIds().isEmpty()) {
            List<Fetus> fetuses = source.fetusIds().stream()
                    .map(fetusId -> fetusRepository.findById(fetusId)
                            .orElseThrow(() -> new ObjectNotFoundException("fetus", fetusId)))
                    .collect(Collectors.toList());
            pregnancy.setFetuses(fetuses);
        } else {
            pregnancy.setFetuses(new ArrayList<>());
        }

        return pregnancy;
    }
}