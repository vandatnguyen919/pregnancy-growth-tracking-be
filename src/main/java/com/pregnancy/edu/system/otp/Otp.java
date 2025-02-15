package com.pregnancy.edu.system.otp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Otp {
    private String otp;
    private long timestamp;
}