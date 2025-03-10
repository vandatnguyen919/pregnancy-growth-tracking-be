package com.pregnancy.edu.myuser.converter;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.dto.UserDto;
import com.pregnancy.edu.system.common.BloodType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, MyUser> {

    @Override
    public MyUser convert(UserDto source) {
        MyUser user = new MyUser();
        user.setId(source.id());
        user.setFullName(source.fullName());
        user.setEmail(source.email());
        user.setUsername(source.username());
        user.setEnabled(source.enabled());
        user.setVerified(source.verified());
        user.setRole(source.role());

        // Map additional attributes
        user.setPhoneNumber(source.phoneNumber());
        user.setDateOfBirth(source.dateOfBirth());
        user.setAvatarUrl(source.avatarUrl());
        user.setGender(source.gender());

        // Handle enum conversion if string is not null
        if (source.bloodType() != null) {
            try {
                user.setBloodType(BloodType.valueOf(source.bloodType()));
            } catch (IllegalArgumentException e) {
                // Handle invalid enum value (optional)
                // Could log warning or set to null
            }
        }

        user.setSymptoms(source.symptoms());
        user.setNationality(source.nationality());
        user.setCreatedAt(source.createdAt());
        user.setUpdatedAt(source.updatedAt());

        return user;
    }
}
