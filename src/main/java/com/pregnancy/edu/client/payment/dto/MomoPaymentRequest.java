package com.pregnancy.edu.client.payment.dto;

import lombok.Builder;

@Builder
public record MomoPaymentRequest(
        String partnerCode,
        String accessKey,
        String requestId,
        long amount,
        String orderId,
        String orderInfo,
        int orderExpireTime,
        String redirectUrl,
        String ipnUrl,
        String extraData,
        String requestType,
        String signature,
        String lang
) {}
