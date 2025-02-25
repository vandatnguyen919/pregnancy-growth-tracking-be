package com.pregnancy.edu.membershippackages.payment;

import com.pregnancy.edu.system.common.PaymentProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.Principal;

public interface PaymentService {
    ResponseEntity<?> createPayment(Principal principal,
                                    HttpServletRequest req,
                                    Long subscriptionId,
                                    PaymentProvider provider);

    ResponseEntity<?> checkVNPayPayment(
            Principal principal,
            HttpServletRequest req,
            String vnp_TxnRef,
            String vnp_TransDate
    ) throws IOException;
}
