package com.pregnancy.edu.myuser.converter;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, MyUser> {

    @Override
    public MyUser convert(UserDto source) {
        MyUser user = new MyUser();
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setRole(source.role());
        return user;
    }
}
