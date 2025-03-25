package com.pregnancy.edu.admin.dto;

import java.util.Map;

public record RoleDistributionDto(
        Map<String, Long> roleCounts,
        Long totalUsers
) {
}
