package com.pregnancy.edu.membershippackages.order.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Long membershipPlanId;
    private String provider; // truyen ten cua provider luon (VNPAY, MOMO)
}
