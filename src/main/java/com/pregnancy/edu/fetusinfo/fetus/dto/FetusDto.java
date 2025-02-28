package com.pregnancy.edu.fetusinfo.fetus.dto;

import jakarta.validation.constraints.NotNull;

public record FetusDto(
        Long id,
        @NotNull(message = "User ID is required")
        Long userId,
        @NotNull(message = "Pregnancy ID is required")
        Long pregnancyId,
        String nickName,
        String gender
) {
}