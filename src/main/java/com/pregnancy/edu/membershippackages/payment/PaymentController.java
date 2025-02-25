package com.pregnancy.edu.membershippackages.payment;

import com.pregnancy.edu.client.payment.VNPayPaymentClient;
import com.pregnancy.edu.client.payment.dto.CreatePaymentResponse;
import com.pregnancy.edu.client.payment.dto.VNPayQueryResponse;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.common.PaymentProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final VNPayPaymentClient vnPayPaymentClient;

    public PaymentController(VNPayPaymentClient vnPayPaymentClient) {
        this.vnPayPaymentClient = vnPayPaymentClient;
    }

    /**
     * Creates a VNPay payment URL.
     * Example URL: GET /api/v1/payment/create/vnpay?amount=100000
     * @param amount  the amount to be paid
     * @param request the HTTP servlet request
     * @return a Result object containing the payment URL and VNPay provider
     */
    @GetMapping("/create/vnpay")
    public Result createVNPayPayment(
            @Param(value = "amount") Long amount,
            HttpServletRequest request
    ) {
        CreatePaymentResponse createPaymentResponse = vnPayPaymentClient.createPayment(amount, request);
        return new Result(true, StatusCode.SUCCESS, "Payment Url Created Success", createPaymentResponse);
    }

    /**
     * Checks the status of a VNPay payment.
     * Example URL: GET /api/v1/payment/check/vnpay?vnp_TxnRef=12345&vnp_PayDate=20230101
     *
     * @param vnp_TxnRef the transaction reference number
     * @param vnp_TransDate the transaction date
     * @param request the HTTP servlet request
     * @return a Result object containing the status of the payment check
     */
    @GetMapping("/check/vnpay")
    public Result checkVNPayPayment(
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(name = "vnp_PayDate") String vnp_TransDate,
            HttpServletRequest request
    ) {
        VNPayQueryResponse vnPayQueryResponse = vnPayPaymentClient.queryTransaction(vnp_TxnRef, vnp_TransDate, request);
        if (!"00".equals(vnPayQueryResponse.vnp_ResponseCode())) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Checked Failed", vnPayQueryResponse);
        }
        return new Result(true, StatusCode.SUCCESS, "Payment Checked Success");
    }

//    @GetMapping("/create-payment")
//    public ResponseEntity<?> createPayment(
//            Principal principal,
//            HttpServletRequest req,
//            @RequestParam Long subscriptionId,
//            @RequestParam(required = false, defaultValue = "VNPAY") PaymentProvider provider
//    ) {
//        return paymentService.createPayment(principal, req, subscriptionId, provider);
//    }
//
//    @GetMapping("/check-payment/vnpay")
//    public ResponseEntity<?> checkVNPayPayment(
//            Principal principal,
//            HttpServletRequest req,
//            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
//            @RequestParam(name = "vnp_PayDate") String vnp_TransDate
//    ) throws IOException {
//        return paymentService.checkVNPayPayment(principal, req, vnp_TxnRef, vnp_TransDate);
//    }
}