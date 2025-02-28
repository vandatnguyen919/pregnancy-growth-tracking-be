package com.pregnancy.edu.client.payment.dto;

public record MomoQueryRequest(
    String partnerCode,
    String requestId,
    String orderId,
    String lang,
    String signature
) {}