package com.pregnancy.edu.membershippackages.subscription.converter;

import com.pregnancy.edu.membershippackages.subscription.Subscription;
import com.pregnancy.edu.membershippackages.subscription.dto.SubscriptionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SubscriptionToSubscriptionDtoConverter implements Converter<Subscription, SubscriptionDto> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public SubscriptionDto convert(Subscription source) {
        return new SubscriptionDto(
                source.getId(),
                source.getStartDate(),
                source.getEndDate(),
                source.getStatus(),
                source.getCreatedAt(),
                source.getUpdatedAt(),
                source.getUser().getId() == null ? null : source.getUser().getId(),
                source.getMembershipPlans() == null ? null : source.getMembershipPlans().get(0).getId()
        );
    }
}
