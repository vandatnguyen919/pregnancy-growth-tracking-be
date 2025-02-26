package com.pregnancy.edu.membershippackages.order.dto;

public record OrderDto(
        Long id,
        Double amount,
        String provider,
        String currency,
        String transactionId,
        String transactionDate,
        Long membershipPlanId,
        Long userId
) {}