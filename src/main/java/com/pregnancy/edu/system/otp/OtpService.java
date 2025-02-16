package com.pregnancy.edu.system.otp;

import com.pregnancy.edu.security.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class OtpService {
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes in milliseconds
    
    private final Map<String, Otp> otpMap = new HashMap<>();

    private final AuthService authService;
    
    private final JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String senderEmail;

    public OtpService(AuthService authService, JavaMailSender mailSender) {
        this.authService = authService;
        this.mailSender = mailSender;
    }

    public String generateOTP(String userEmail) {
        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        
        // Save OTP and its expiration time
        otpMap.put(userEmail, new Otp(otp, System.currentTimeMillis()));
        
        // Send OTP via email
        sendOtpEmail(userEmail, otp);
        
        return otp;
    }
    
    private void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(toEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp + "\nThis code will expire in 5 minutes.");
            
            mailSender.send(message);
            log.info("OTP email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send OTP email", e);
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
    
    public boolean validateOTP(String userEmail, String otp) {
        Otp otpData = otpMap.get(userEmail);
        
        if (otpData == null) {
            return false;
        }
        
        // Check if OTP is expired
        if (System.currentTimeMillis() - otpData.getTimestamp() > OTP_VALID_DURATION) {
            otpMap.remove(userEmail);
            return false;
        }
        
        // Validate OTP
        if (otpData.getOtp().equals(otp)) {
            otpMap.remove(userEmail);
            return true;
        }
        
        return false;
    }

    public boolean validateEmail(String userEmail, String otp) {
        if (validateOTP(userEmail, otp)) {
            authService.verifyUser(userEmail);
            return true;
        } else {
            return false;
        }
    }
}