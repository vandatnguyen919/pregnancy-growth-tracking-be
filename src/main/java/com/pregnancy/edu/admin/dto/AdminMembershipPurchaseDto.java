package com.pregnancy.edu.admin.dto;

import java.time.LocalDateTime;

public record AdminMembershipPurchaseDto(
        Long orderId,
        String userName,
        String userEmail,
        String planName,
        Double planPrice,
        Integer planDuration,
        LocalDateTime purchaseDate
) {
}