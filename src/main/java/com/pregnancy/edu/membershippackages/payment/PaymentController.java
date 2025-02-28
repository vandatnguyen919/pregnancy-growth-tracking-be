package com.pregnancy.edu.membershippackages.payment;

import com.pregnancy.edu.client.payment.MomoPaymentClient;
import com.pregnancy.edu.client.payment.PaymentClient;
import com.pregnancy.edu.client.payment.VNPayPaymentClient;
import com.pregnancy.edu.client.payment.dto.PaymentCreationResponse;
import com.pregnancy.edu.client.payment.dto.MomoQueryResponse;
import com.pregnancy.edu.client.payment.dto.PaymentQueryResponse;
import com.pregnancy.edu.client.payment.dto.VNPayQueryResponse;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentClient vnPayPaymentClient;

    private final PaymentClient momoPaymentClient;

    public PaymentController(
            @Qualifier("vnPayPaymentClient") PaymentClient vnPayPaymentClient,
            @Qualifier("momoPaymentClient") PaymentClient momoPaymentClient) {
        this.vnPayPaymentClient = vnPayPaymentClient;
        this.momoPaymentClient = momoPaymentClient;
    }

    /**
     * Creates a VNPay payment URL.
     * Example URL: GET /api/v1/payment/create/vnpay?amount=100000
     *
     * @param amount  the amount to be paid
     * @return a Result object containing the payment URL and VNPay provider
     */
    @GetMapping("/create/vnpay")
    public Result createVNPayPayment(
            @Param(value = "amount") Long amount
    ) {
        PaymentCreationResponse paymentCreationResponse = vnPayPaymentClient.createPayment(amount);
        return new Result(true, StatusCode.SUCCESS, "Payment Url Created Success", paymentCreationResponse);
    }

    /**
     * Creates a Momo payment URL.
     * Example URL: GET /api/v1/payment/create/momo?amount=100000
     *
     * @param amount  the amount to be paid
     * @return a Result object containing the payment URL and Momo provider
     */
    @GetMapping("/create/momo")
    public Result createMomoPayment(
            @Param(value = "amount") Long amount
    ) {
        PaymentCreationResponse paymentCreationResponse = momoPaymentClient.createPayment(amount);
        return new Result(true, StatusCode.SUCCESS, "Payment Url Created Success", paymentCreationResponse);
    }

    /**
     * Checks the status of a VNPay payment.
     * Example URL: GET /api/v1/payment/check/vnpay?vnp_TxnRef=59796198
     *
     * @param vnp_TxnRef    the transaction reference number
     * @return a Result object containing the status of the payment check
     */
    @GetMapping("/check/vnpay")
    public Result checkVNPayPayment(
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef
    ) {
        PaymentQueryResponse paymentQueryResponse = vnPayPaymentClient.queryPayment(vnp_TxnRef);
        if (paymentQueryResponse.code() != 0) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Checked Failed", paymentQueryResponse);
        }
        return new Result(true, StatusCode.SUCCESS, "Payment Checked Success", paymentQueryResponse);
    }

    /**
     * Checks the status of a Momo payment.
     * Example URL: GET /api/v1/payment/check/momo?orderId=MOMO1740729694232
     *
     * @param orderId the order ID to be checked
     * @return a Result object containing the status of the payment check
     */
    @GetMapping("/check/momo")
    public Result checkMomoPayment(
            @RequestParam(name = "orderId") String orderId
    ) {
        PaymentQueryResponse paymentQueryResponse = momoPaymentClient.queryPayment(orderId);
        if (paymentQueryResponse.code() != 0) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Checked Failed", paymentQueryResponse);
        }
        return new Result(true, StatusCode.SUCCESS, "Payment Checked Success", paymentQueryResponse);
    }
}