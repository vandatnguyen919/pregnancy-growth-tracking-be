package com.pregnancy.edu.client.payment.dto;

import com.pregnancy.edu.system.common.PaymentProvider;

public record CreatePaymentResponse(
        PaymentProvider provider,
        String paymentUrl
) {
}
