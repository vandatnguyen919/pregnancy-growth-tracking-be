package com.pregnancy.edu.pregnancy.dto;

import java.time.LocalDate;

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