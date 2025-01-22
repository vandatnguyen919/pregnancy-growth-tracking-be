package com.pregnancy.edu.preguser.converter;

import com.pregnancy.edu.preguser.MyUser;
import com.pregnancy.edu.preguser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, MyUser> {

    @Override
    public MyUser convert(UserDto source) {
        MyUser user = new MyUser();
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRole("admin");
        return user;
    }
}
