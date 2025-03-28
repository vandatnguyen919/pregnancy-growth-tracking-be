package com.pregnancy.edu.membershippackages.order.dto;

public record OrderDto(
        Long id,
        Double amount,
        String provider,
        String currency,
        String transactionId,
        String transactionDate,
        String status,
        String createdAt,
        String startDate,
        String endDate,
        Long membershipPlanId,
        Long userId
) {
}