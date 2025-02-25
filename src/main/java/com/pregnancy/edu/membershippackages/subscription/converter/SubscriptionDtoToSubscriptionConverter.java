package com.pregnancy.edu.membershippackages.subscription.converter;

import com.pregnancy.edu.membershippackages.subscription.Subscription;
import com.pregnancy.edu.membershippackages.subscription.dto.SubscriptionDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SubscriptionDtoToSubscriptionConverter implements Converter<SubscriptionDto, Subscription> {


    @Override
    public Subscription convert(SubscriptionDto source) {
        Subscription subscription = new Subscription();
        subscription.setId(source.id());
        subscription.setStartDate(source.startDate());
        subscription.setEndDate(source.endDate());
        subscription.setStatus(source.status());
        subscription.getUser().setId(source.userId());
        subscription.getMembershipPlans().get(0).setId(source.paymentId());
        return subscription;
    }
}