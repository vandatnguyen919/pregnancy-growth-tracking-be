package com.pregnancy.edu.client.payment.dto;

import java.util.List;

public record MomoQueryResponse(
    String partnerCode,
    String orderId,
    String requestId,
    String extraData,
    int amount,
    long transId,
    String payType,
    int resultCode,
    List<Object> refundTrans,
    String message,
    long responseTime,
    long lastUpdated,
    Object signature
) {}