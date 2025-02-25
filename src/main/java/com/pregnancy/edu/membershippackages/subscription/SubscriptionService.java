package com.pregnancy.edu.membershippackages.subscription;

import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanRepository;
import com.pregnancy.edu.membershippackages.subscription.dto.SubscriptionDto;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Transactional
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               UserRepository userRepository,
                               MembershipPlanRepository membershipPlanRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
        this.membershipPlanRepository = membershipPlanRepository;
    }

    public SubscriptionDto createSubscription(Long userId, Long membershipPlanId) {
        MyUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

        MembershipPlan plan = membershipPlanRepository.findById(membershipPlanId)
                .orElseThrow(() -> new ObjectNotFoundException("membership plan", membershipPlanId));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(LocalDateTime.now().plusDays(plan.getDurationInDays()));
        subscription.setStatus("PENDING"); // Will be updated after payment

        // Initialize the list if it's null
        if (subscription.getMembershipPlans() == null) {
            subscription.setMembershipPlans(new ArrayList<>());
        }
        subscription.getMembershipPlans().add(plan);

        // Set creation time
        subscription.setCreatedAt(LocalDateTime.now());
        subscription.setUpdatedAt(LocalDateTime.now());

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(savedSubscription);
    }

    public SubscriptionDto updateSubscriptionStatus(Long subscriptionId, String status) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ObjectNotFoundException("subscription", subscriptionId));

        subscription.setStatus(status);
        return convertToDto(subscriptionRepository.save(subscription));
    }

    private SubscriptionDto convertToDto(Subscription subscription) {
        return new SubscriptionDto(
                subscription.getId(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getStatus(),
                subscription.getCreatedAt(),
                subscription.getUpdatedAt(),
                subscription.getUser().getId(),
                subscription.getPayment() != null ? subscription.getPayment().getId() : null
        );
    }
}
