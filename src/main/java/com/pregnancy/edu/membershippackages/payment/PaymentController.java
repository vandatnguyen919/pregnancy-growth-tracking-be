package com.pregnancy.edu.membershippackages.payment;

import com.pregnancy.edu.system.common.PaymentProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(
            Principal principal,
            HttpServletRequest req,
            @RequestParam Long subscriptionId,
            @RequestParam(required = false, defaultValue = "VNPAY") PaymentProvider provider
    ) {
        return paymentService.createPayment(principal, req, subscriptionId, provider);
    }

    @GetMapping("/check-payment/vnpay")
    public ResponseEntity<?> checkVNPayPayment(
            Principal principal,
            HttpServletRequest req,
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(name = "vnp_PayDate") String vnp_TransDate
    ) throws IOException {
        return paymentService.checkVNPayPayment(principal, req, vnp_TxnRef, vnp_TransDate);
    }

}