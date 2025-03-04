package com.pregnancy.edu.client.payment;

import com.pregnancy.edu.client.payment.dto.PaymentCreationResponse;
import com.pregnancy.edu.client.payment.dto.PaymentQueryResponse;

public interface PaymentClient {

    /**
     * Creates a payment request and returns a URL for the payment page.
     *
     * @param amount  Amount to be paid in VND
     * @return Response containing payment URL
     */
    PaymentCreationResponse createPayment(long amount);
    
    PaymentQueryResponse queryPayment(String transactionId);
}
