package com.pregnancy.edu.membershippackages.membership.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MembershipPlanDto(
        Long id,
        @NotEmpty(message = "Name is required")
        String name,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        Double price,
        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be positive")
        Integer durationMonths,
        Boolean isActive,
        Long subscriptionId
) {
}