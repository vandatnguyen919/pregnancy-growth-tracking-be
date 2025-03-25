package com.pregnancy.edu.admin;

import com.pregnancy.edu.admin.dto.AdminMembershipPurchaseDto;
import com.pregnancy.edu.admin.dto.MembershipPlanStatsDto;
import com.pregnancy.edu.admin.dto.RoleDistributionDto;
import com.pregnancy.edu.admin.dto.UserAgeRangeDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/membership-purchases")
    public Result getMembershipPurchases(Pageable pageable) {
        Page<AdminMembershipPurchaseDto> purchases = adminDashboardService.getMembershipPurchases(pageable);
        return new Result(true, StatusCode.SUCCESS, "Find Membership Purchases Success", purchases);
    }

    @GetMapping("/membership-plan-stats")
    public Result getMembershipPlanStats() {
        MembershipPlanStatsDto stats = adminDashboardService.getMembershipPlanStats();
        return new Result(true, StatusCode.SUCCESS, "Find Membership Plan Stats Success", stats);
    }

    @GetMapping("/user-age-ranges")
    public Result getUserAgeRanges() {
        UserAgeRangeDto ageRanges = adminDashboardService.getUserAgeRanges();
        return new Result(true, StatusCode.SUCCESS, "Find User Age Ranges Success", ageRanges);
    }

    @GetMapping("/role-distribution")
    public Result getRoleDistribution() {
        RoleDistributionDto roleDistribution = adminDashboardService.getRoleDistribution();
        return new Result(true, StatusCode.SUCCESS, "Find Role Distribution Success", roleDistribution);
    }

    @GetMapping("/membership-plan-user-distribution")
    public Result getMembershipPlanUserDistribution() {
        Map<String, Object> membershipPlanDistribution = adminDashboardService.getMembershipPlanUserDistribution();
        return new Result(true, StatusCode.SUCCESS, "Find Membership Plan User Distribution Success", membershipPlanDistribution);
    }
}
