package com.pregnancy.edu.myuser.converter;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.dto.UserDto;
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
