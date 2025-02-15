package com.pregnancy.edu.security;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.MyUserPrincipal;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.myuser.converter.UserToUserDtoConverter;
import com.pregnancy.edu.myuser.dto.UserDto;
import com.pregnancy.edu.security.dto.RegisterDto;
import com.pregnancy.edu.system.common.Role;
import com.pregnancy.edu.system.exception.RegisterIllegalArgumentException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {

    private final UserToUserDtoConverter userToUserDtoConverter;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AuthService(UserToUserDtoConverter userToUserDtoConverter, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userToUserDtoConverter = userToUserDtoConverter;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {

        // Create user info.
        MyUserPrincipal principal = (MyUserPrincipal)authentication.getPrincipal();
        MyUser myUser = principal.getMyUser();
        UserDto userDto = userToUserDtoConverter.convert(myUser);

        // Create a JWT
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);
        return loginResultMap;
    }

    public UserDto createRegisterInfo(RegisterDto registerDto) {

        // If the email is already taken, throw an exception.
        if (userRepository.findByEmail(registerDto.email()).isPresent()) {
            throw new RegisterIllegalArgumentException("Email is already taken.");
        }

        // If the password and confirm password do not match, throw an exception.
        if (!registerDto.password().equals(registerDto.confirmPassword())) {
            throw new RegisterIllegalArgumentException("New password and confirm new password do not match.");
        }

        MyUser myUser = new MyUser();
        myUser.setFullName(registerDto.fullName());
        myUser.setEmail(registerDto.email());
        myUser.setUsername(UUID.randomUUID().toString());
        myUser.setPassword(passwordEncoder.encode(registerDto.password()));
        myUser.setEnabled(false);
        myUser.setRole(Role.USER.getDisplayName()); // Default role is user

        MyUser newUser = userRepository.save(myUser);

        return userToUserDtoConverter.convert(newUser);
    }

    public void enableUser(String email) {
        MyUser myUser = userRepository.findByEmail(email).orElseThrow(() -> new RegisterIllegalArgumentException("User not found."));
        myUser.setEnabled(true);
        userRepository.save(myUser);
    }
}
