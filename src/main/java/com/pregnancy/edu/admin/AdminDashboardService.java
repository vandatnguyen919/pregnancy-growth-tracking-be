package com.pregnancy.edu.admin;

import com.pregnancy.edu.admin.dto.AdminMembershipPurchaseDto;
import com.pregnancy.edu.admin.dto.MembershipPlanStatsDto;
import com.pregnancy.edu.admin.dto.RoleDistributionDto;
import com.pregnancy.edu.admin.dto.UserAgeRangeDto;
import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanRepository;
import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.membershippackages.order.OrderRepository;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.common.AgeRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminDashboardService {

    private final OrderRepository orderRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final UserRepository userRepository;

    public AdminDashboardService(
            OrderRepository orderRepository,
            MembershipPlanRepository membershipPlanRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.membershipPlanRepository = membershipPlanRepository;
        this.userRepository = userRepository;
    }

    public Page<AdminMembershipPurchaseDto> getMembershipPurchases(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);

        return orders.map(order -> new AdminMembershipPurchaseDto(
                order.getId(),
                order.getUser().getFullName(),
                order.getUser().getEmail(),
                order.getMembershipPlan().getName(),
                order.getMembershipPlan().getPrice(),
                order.getMembershipPlan().getDurationMonths(),
                order.getCreatedAt()
        ));
    }

    public MembershipPlanStatsDto getMembershipPlanStats() {
        List<Order> orders = orderRepository.findAll();

        Long totalPurchases = (long) orders.size();

        Double totalRevenue = orders.stream()
                .mapToDouble(order -> order.getMembershipPlan().getPrice())
                .sum();

        Map<Long, Long> planCountMap = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getMembershipPlan().getId(),
                        Collectors.counting()
                ));
        return new MembershipPlanStatsDto(totalPurchases, totalRevenue);
    }

    public UserAgeRangeDto getUserAgeRanges() {
        List<MyUser> users = userRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        Map<String, Long> ageRangeCounts = new HashMap<>();

        users.forEach(user -> {
            if (user.getDateOfBirth() != null) {
                int age = Period.between(user.getDateOfBirth().toLocalDate(), now.toLocalDate()).getYears();

                String ageRange = getAgeRange(age);
                ageRangeCounts.merge(ageRange, 1L, Long::sum);
            }
        });

        return new UserAgeRangeDto(ageRangeCounts, (long) users.size());
    }

    private String getAgeRange(int age) {
        if (age < 25) return AgeRange.AGE_18_24.getDisplayName();
        if (age < 35) return AgeRange.AGE_25_34.getDisplayName();
        if (age < 45) return AgeRange.AGE_35_44.getDisplayName();
        if (age < 55) return AgeRange.AGE_45_54.getDisplayName();
        if (age < 65) return AgeRange.AGE_55_64.getDisplayName();
        return AgeRange.AGE_65_PLUS.getDisplayName();
    }

    public RoleDistributionDto getRoleDistribution() {
        List<MyUser> users = userRepository.findAll();

        Map<String, Long> roleCounts = users.stream()
                .collect(Collectors.groupingBy(
                        MyUser::getRole,
                        Collectors.counting()
                ));

        return new RoleDistributionDto(roleCounts, (long) users.size());
    }

    public Map<String, Object> getMembershipPlanUserDistribution() {
        List<MembershipPlan> plans = membershipPlanRepository.findAll();
        List<Order> orders = orderRepository.findAll();

        Map<Long, List<Order>> ordersByPlan = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getMembershipPlan().getId()
                ));

        List<Map<String, Object>> planDistribution = plans.stream()
                .map(plan -> {
                    List<Order> planOrders = ordersByPlan.getOrDefault(plan.getId(), List.of());

                    List<MyUser> uniqueUsers = planOrders.stream()
                            .map(Order::getUser)
                            .distinct()
                            .collect(Collectors.toList());

                    Map<String, Object> planInfo = new HashMap<>();
                    planInfo.put("planId", plan.getId());
                    planInfo.put("planName", plan.getName());
                    planInfo.put("totalPurchases", (long) planOrders.size());
                    planInfo.put("uniqueUsers", (long) uniqueUsers.size());
                    planInfo.put("price", plan.getPrice());
                    planInfo.put("durationMonths", plan.getDurationMonths());

                    return planInfo;
                })
                .collect(Collectors.toList());

        long totalPurchases = orders.size();
        long totalUniquePurchasers = orders.stream()
                .map(Order::getUser)
                .distinct()
                .count();

        return Map.of(
                "planDistribution", planDistribution,
                "totalPurchases", totalPurchases,
                "totalUniquePurchasers", totalUniquePurchasers
        );
    }
}