package com.pregnancy.edu.membershippackages.order.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private Long userId;
    private Long membershipPlanId;
    private Double amount;
    private String provider;
    private String transactionId;
}
