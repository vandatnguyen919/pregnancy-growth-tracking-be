package com.pregnancy.edu.membershippackages.order.dto;

public record OrderPaymentResponse(
        Long id,
        Double amount,
        String provider,
        String currency,
        String transactionId,
        String transactionDate,
        String membershipPlan,
        Long userId
) {}