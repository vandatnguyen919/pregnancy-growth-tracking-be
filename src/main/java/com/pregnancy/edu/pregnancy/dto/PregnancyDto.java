package com.pregnancy.edu.pregnancy.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PregnancyDto(
        Long id,
        @NotNull(message = "User ID is required")
        Long userId,
        Integer maternalAge,
        LocalDate pregnancyStartDate,
        LocalDate estimatedDueDate,
        LocalDate deliveryDate,
        String status,
        List<Long> fetusIds
) {
}