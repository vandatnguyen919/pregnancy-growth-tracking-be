package com.pregnancy.edu.security;

import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.myuser.converter.UserToUserDtoConverter;
import com.pregnancy.edu.myuser.dto.UserDto;
import com.pregnancy.edu.security.dto.RegisterDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class.getName());

    private final AuthService authService;

    private final UserService userService;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthController(AuthService authService, UserService userService, UserToUserDtoConverter userToUserDtoConverter) {
        this.authService = authService;
        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @PostMapping("/login")
    public Result getLoginInfo(Authentication authentication) {
        LOGGER.debug("Authenticated user: '{}'", authentication.getName());
        return new Result(true, StatusCode.SUCCESS, "User Info and JSON Web Token", this.authService.createLoginInfo(authentication));
    }

    @PostMapping("/register")
    public Result getRegisterInfo(@Valid @RequestBody RegisterDto registerDto) {
        return new Result(true, StatusCode.SUCCESS, "User Registration", this.authService.createRegisterInfo(registerDto));
    }

    @GetMapping("/profile")
    public Result getUserInfo(JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        MyUser myUser = this.userService.findById(userId);
        UserDto userDto = this.userToUserDtoConverter.convert(myUser);
        return new Result(true, StatusCode.SUCCESS, "User Info", userDto);
    }
}
