package com.pregnancy.edu.client.payment.dto;

import com.pregnancy.edu.system.common.PaymentProvider;

public record PaymentCreationResponse(
        PaymentProvider provider,
        String paymentUrl
) {
}
