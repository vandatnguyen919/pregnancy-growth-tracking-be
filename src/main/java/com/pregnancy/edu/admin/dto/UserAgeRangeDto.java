package com.pregnancy.edu.admin.dto;

import java.util.Map;

public record UserAgeRangeDto(
        Map<String, Long> ageRangeCounts,
        Long totalUsers
) {
}