package com.pregnancy.edu.membershippackages.membership;

import com.pregnancy.edu.membershippackages.membership.converter.MembershipPlanToMembershipPlanDtoConverter;
import com.pregnancy.edu.membershippackages.membership.dto.MembershipPlanDto;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class MembershipPlanService {
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipPlanToMembershipPlanDtoConverter toMembershipPlanDtoConverter;

    public MembershipPlanService(MembershipPlanRepository membershipPlanRepository,
                                 MembershipPlanToMembershipPlanDtoConverter toMembershipPlanDtoConverter) {
        this.membershipPlanRepository = membershipPlanRepository;
        this.toMembershipPlanDtoConverter = toMembershipPlanDtoConverter;
    }

    public List<MembershipPlanDto> findAll() {
        List<MembershipPlan> plans = membershipPlanRepository.findAll();
        return plans.stream()
                .map(toMembershipPlanDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public Page<MembershipPlanDto> findAll(Pageable pageable) {
        Page<MembershipPlan> planPage = membershipPlanRepository.findAll(pageable);
        return planPage.map(toMembershipPlanDtoConverter::convert);
    }

    public MembershipPlanDto findById(Long planId) {
        MembershipPlan plan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new ObjectNotFoundException("membership plan", planId));
        return toMembershipPlanDtoConverter.convert(plan);
    }

    public MembershipPlanDto save(MembershipPlan plan) {
        MembershipPlan savedPlan = membershipPlanRepository.save(plan);
        return toMembershipPlanDtoConverter.convert(savedPlan);
    }

    public MembershipPlanDto update(Long planId, MembershipPlan plan) {
        return membershipPlanRepository.findById(planId)
                .map(existingPlan -> {
                    existingPlan.setName(plan.getName());
                    existingPlan.setPrice(plan.getPrice());
                    existingPlan.setDurationMonths(plan.getDurationMonths());
                    existingPlan.setActive(plan.isActive());

                    MembershipPlan updatedPlan = membershipPlanRepository.save(existingPlan);
                    return toMembershipPlanDtoConverter.convert(updatedPlan);
                })
                .orElseThrow(() -> new ObjectNotFoundException("membership plan", planId));
    }

    public void delete(Long planId) {
        membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new ObjectNotFoundException("membership plan", planId));
        membershipPlanRepository.deleteById(planId);
    }

    public List<MembershipPlanDto> findActiveOnly() {
        List<MembershipPlan> plans = membershipPlanRepository.findByIsActiveTrue();
        return plans.stream()
                .map(toMembershipPlanDtoConverter::convert)
                .collect(Collectors.toList());
    }
}