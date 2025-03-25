package com.pregnancy.edu.admin;

import com.pregnancy.edu.admin.dto.AdminMembershipPurchaseDto;
import com.pregnancy.edu.admin.dto.MembershipPlanStatsDto;
import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanRepository;
import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.membershippackages.order.OrderRepository;
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

    public AdminDashboardService(
            OrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
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
}