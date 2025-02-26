package com.pregnancy.edu.membershippackages.membership.converter;

import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.dto.MembershipPlanDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MembershipPlanToMembershipPlanDtoConverter implements Converter<MembershipPlan, MembershipPlanDto> {
    @Override
    public MembershipPlanDto convert(MembershipPlan source) {
        return new MembershipPlanDto(
                source.getId(),
                source.getName(),
                source.getPrice(),
                source.getDurationMonths(),
                source.isActive(),
                source.getOrder() != null ? source.getOrder().getId() : null
        );
    }
}