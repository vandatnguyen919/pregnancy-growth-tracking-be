package com.pregnancy.edu.admin;

import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanRepository;
import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.membershippackages.order.OrderRepository;
import com.pregnancy.edu.myuser.MyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminDashboardService {

    private final OrderRepository orderRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    public AdminDashboardService(
            OrderRepository orderRepository,
            MembershipPlanRepository membershipPlanRepository
    ) {
        this.orderRepository = orderRepository;
        this.membershipPlanRepository = membershipPlanRepository;
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

        // Total purchases
        Long totalPurchases = (long) orders.size();

        // Total revenue
        Double totalRevenue = orders.stream()
                .mapToDouble(order -> order.getMembershipPlan().getPrice())
                .sum();

        // Plan breakdown
        Map<Long, Long> planCountMap = orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getMembershipPlan().getId(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> planBreakdown = new ArrayList<>();

        List<MembershipPlan> allPlans = membershipPlanRepository.findAll();

        for (MembershipPlan plan : allPlans) {
            Map<String, Object> planInfo = new HashMap<>();
            planInfo.put("planId", plan.getId());
            planInfo.put("planName", plan.getName());
            planInfo.put("totalPurchases", planCountMap.getOrDefault(plan.getId(), 0L));
            planInfo.put("price", plan.getPrice());
            planInfo.put("durationMonths", plan.getDurationMonths());

            planBreakdown.add(planInfo);
        }

        return new MembershipPlanStatsDto(totalPurchases, totalRevenue, planBreakdown);
    }
}