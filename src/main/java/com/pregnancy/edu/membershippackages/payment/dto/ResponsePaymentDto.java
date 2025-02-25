package com.pregnancy.edu.membershippackages.payment.dto;

import com.pregnancy.edu.system.common.PaymentProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePaymentDto {
    private PaymentProvider provider;
    private String paymentUrl;
}