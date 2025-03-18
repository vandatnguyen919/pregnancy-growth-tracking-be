package com.pregnancy.edu.pregnancy.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record PregnancyDto(
        Long id,
        Long userId,
        Integer maternalAge,
        LocalDate pregnancyStartDate,
        LocalDate dueDate,
        LocalDate deliveryDate,
        String status,
        int fetusCount
) {
}