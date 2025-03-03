package com.pregnancy.edu.membershippackages.order;

import com.pregnancy.edu.client.payment.dto.PaymentQueryResponse;
import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanRepository;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.common.PaymentProvider;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        MembershipPlanRepository membershipPlanRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    public Order createInitialOrder(Long userId, Long membershipPlanId, Double amount, PaymentProvider provider, String transactionId) {
        MyUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MembershipPlan membershipPlan = membershipPlanRepository.findById(membershipPlanId)
                .orElseThrow(() -> new IllegalArgumentException("Membership plan not found"));

        Order order = new Order();
        order.setUser(user);
        order.setMembershipPlan(membershipPlan);
        order.setAmount(amount);
        order.setProvider(provider.name());
        order.setCurrency("VND");
        order.setTransactionId(transactionId);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(String transactionId, PaymentQueryResponse paymentResponse) {
        Order order = orderRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for transaction: " + transactionId));

        if (paymentResponse.code() == 0) {
            order.setStatus("COMPLETED");
            order.setTransactionDate(LocalDateTime.now());

            LocalDateTime startDate = LocalDateTime.now();
            order.setStartDate(startDate);

            int durationInDays = order.getMembershipPlan().getDurationInDays();
            order.setEndDate(startDate.plusDays(durationInDays));
        } else {
            order.setStatus("FAILED");
        }

        return orderRepository.save(order);
    }
}


