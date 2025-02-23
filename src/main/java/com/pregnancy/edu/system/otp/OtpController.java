package com.pregnancy.edu.system.otp;

import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/generate")
    public Result generateOTP(@RequestParam String email) {
        try {
            String otp = otpService.generateOTP(email);
            return new Result(true, StatusCode.SUCCESS, "OTP sent successfully to " + email);
        } catch (Exception e) {
            return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, "Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public Result validateOTP(
            @RequestParam String email,
            @RequestParam String otp) {
        boolean isValid = otpService.validateOTP(email, otp);
        if (isValid) {
            return new Result(true, StatusCode.SUCCESS, "OTP validated successfully");
        } else {
            return new Result(false, StatusCode.FORBIDDEN, "Invalid or expired OTP");
        }
    }

    @PostMapping("/validate-email")
    public Result validateEmail(
            @RequestParam String email,
            @RequestParam String otp) {
        boolean isValid = otpService.validateEmail(email, otp);
        if (isValid) {
            return new Result(true, StatusCode.SUCCESS, "OTP validated successfully");
        } else {
            return new Result(false, StatusCode.FORBIDDEN, "Invalid or expired OTP");
        }
    }
}