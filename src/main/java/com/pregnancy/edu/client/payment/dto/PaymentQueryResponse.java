package com.pregnancy.edu.client.payment.dto;

import com.pregnancy.edu.system.common.PaymentStatus;

public record PaymentQueryResponse(
        int code,
        String message,
        PaymentStatus status
) {
}
