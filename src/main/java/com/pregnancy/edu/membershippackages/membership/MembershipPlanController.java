package com.pregnancy.edu.membershippackages.membership;

import com.pregnancy.edu.membershippackages.membership.converter.MembershipPlanDtoToMembershipPlanConverter;
import com.pregnancy.edu.membershippackages.membership.dto.MembershipPlanDto;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership-plans")
@Slf4j
public class MembershipPlanController {
    private final MembershipPlanService membershipPlanService;
    private final MembershipPlanDtoToMembershipPlanConverter toMembershipPlanConverter;

    public MembershipPlanController(MembershipPlanService membershipPlanService,
                                    MembershipPlanDtoToMembershipPlanConverter toMembershipPlanConverter) {
        this.membershipPlanService = membershipPlanService;
        this.toMembershipPlanConverter = toMembershipPlanConverter;
    }

    @GetMapping
    public Result getAllMembershipPlans(Pageable pageable) {
        Page<MembershipPlanDto> planDtoPage = membershipPlanService.findAll(pageable);
        return new Result(true, StatusCode.SUCCESS, "Find All Success", planDtoPage);
    }

    @GetMapping("/active")
    public Result getActiveMembershipPlans() {
        List<MembershipPlanDto> activePlans = membershipPlanService.findActiveOnly();
        return new Result(true, StatusCode.SUCCESS, "Find Active Plans Success", activePlans);
    }

    @GetMapping("/{planId}")
    public Result getMembershipPlanById(@PathVariable Long planId) {
        MembershipPlanDto planDto = membershipPlanService.findById(planId);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", planDto);
    }

    @PostMapping
    public Result addMembershipPlan(@Valid @RequestBody MembershipPlanDto newPlanDto) {
        MembershipPlan newPlan = toMembershipPlanConverter.convert(newPlanDto);
        MembershipPlanDto savedPlanDto = membershipPlanService.save(newPlan);
        return new Result(true, StatusCode.SUCCESS, "Add Success", savedPlanDto);
    }

    @PutMapping("/{planId}")
    public Result updateMembershipPlan(@PathVariable Long planId, @Valid @RequestBody MembershipPlanDto planDto) {
        MembershipPlan update = toMembershipPlanConverter.convert(planDto);
        MembershipPlanDto updatedPlanDto = membershipPlanService.update(planId, update);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedPlanDto);
    }

    @DeleteMapping("/{planId}")
    public Result deleteMembershipPlan(@PathVariable Long planId) {
        membershipPlanService.delete(planId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}