package com.pregnancy.edu.membershippackages.subscription;

import com.pregnancy.edu.membershippackages.subscription.converter.SubscriptionDtoToSubscriptionConverter;
import com.pregnancy.edu.membershippackages.subscription.converter.SubscriptionToSubscriptionDtoConverter;
import com.pregnancy.edu.membershippackages.subscription.dto.SubscriptionDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Slf4j
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final SubscriptionToSubscriptionDtoConverter toSubscriptionDtoConverter;
    private final SubscriptionDtoToSubscriptionConverter toSubscriptionConverter;

    public SubscriptionController(SubscriptionService subscriptionService,
                                  SubscriptionToSubscriptionDtoConverter toSubscriptionDtoConverter,
                                  SubscriptionDtoToSubscriptionConverter toSubscriptionConverter) {
        this.subscriptionService = subscriptionService;
        this.toSubscriptionDtoConverter = toSubscriptionDtoConverter;
        this.toSubscriptionConverter = toSubscriptionConverter;
    }

    @PostMapping
    public Result createSubscription(
            @RequestParam Long userId,
            @RequestParam Long membershipPlanId) {
        SubscriptionDto subscriptionDto = subscriptionService.createSubscription(userId, membershipPlanId);
        return new Result(true, StatusCode.SUCCESS, "Subscription created", subscriptionDto);
    }

    @PutMapping("/{subscriptionId}/status")
    public Result updateStatus(
            @PathVariable Long subscriptionId,
            @RequestParam String status) {
        SubscriptionDto subscriptionDto = subscriptionService.updateSubscriptionStatus(subscriptionId, status);
        return new Result(true, StatusCode.SUCCESS, "Status updated", subscriptionDto);
    }
}