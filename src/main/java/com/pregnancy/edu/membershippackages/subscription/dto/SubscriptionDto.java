package com.pregnancy.edu.membershippackages.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionDto(Long id,
                             LocalDateTime startDate,
                             LocalDateTime endDate,
                             String status,
                             LocalDateTime createdAt,
                             LocalDateTime updatedAt,
                             Long userId,
                             Long paymentId) {
}
