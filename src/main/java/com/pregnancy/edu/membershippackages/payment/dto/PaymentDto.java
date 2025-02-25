package com.pregnancy.edu.membershippackages.payment.dto;

public record PaymentDto(
        Long id,
        Double amount,
        String provider,
        String currency,
        String transactionId,
        String transactionDate,
        Long subscriptionId,
        Long userId
) {}