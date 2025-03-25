package com.pregnancy.edu.admin;

import java.util.List;
import java.util.Map;

public record MembershipPlanStatsDto(
        Long totalPurchases,
        Double totalRevenue,
        List<Map<String, Object>> planBreakdown
) {
}