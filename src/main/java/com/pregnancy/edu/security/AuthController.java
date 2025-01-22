package com.pregnancy.edu.security;

import com.pregnancy.edu.security.dto.RegisterDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class.getName());

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
}
