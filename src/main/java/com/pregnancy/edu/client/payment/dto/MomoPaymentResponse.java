package com.pregnancy.edu.client.payment.dto;

public record MomoPaymentResponse(
    String partnerCode,
    String orderId,
    String requestId,
    int amount,
    long responseTime,
    String message,
    int resultCode,
    String payUrl,
    String shortLink
) {}
