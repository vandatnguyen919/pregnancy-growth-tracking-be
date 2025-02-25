package com.pregnancy.edu.membershippackages.membership.converter;

import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.dto.MembershipPlanDto;
import com.pregnancy.edu.membershippackages.subscription.Subscription;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MembershipPlanDtoToMembershipPlanConverter implements Converter<MembershipPlanDto, MembershipPlan> {
    @Override
    public MembershipPlan convert(MembershipPlanDto source) {
        MembershipPlan membershipPlan = new MembershipPlan();
        membershipPlan.setId(source.id());
        membershipPlan.setName(source.name());
        membershipPlan.setPrice(source.price());
        membershipPlan.setDurationMonths(source.durationMonths());
        membershipPlan.setActive(source.isActive() != null ? source.isActive() : false);

        if (source.subscriptionId() != null) {
            Subscription subscription = new Subscription();
            subscription.setId(source.subscriptionId());
            membershipPlan.setSubscription(subscription);
        }

        return membershipPlan;
    }
}