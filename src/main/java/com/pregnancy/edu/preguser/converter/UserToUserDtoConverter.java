package com.pregnancy.edu.preguser.converter;

import com.pregnancy.edu.preguser.MyUser;
import com.pregnancy.edu.preguser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<MyUser, UserDto> {

    @Override
    public UserDto convert(MyUser source) {
        return new UserDto(
                source.getId(),
                source.getEmail(),
                source.getUsername(),
                source.getEnabled(),
                source.getRole()
        );
    }
}
