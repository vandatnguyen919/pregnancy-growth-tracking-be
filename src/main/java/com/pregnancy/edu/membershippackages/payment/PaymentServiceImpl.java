package com.pregnancy.edu.membershippackages.payment;

import com.pregnancy.edu.membershippackages.payment.config.VNPayConfig;
import com.pregnancy.edu.membershippackages.payment.dto.ResponsePaymentDto;
import com.pregnancy.edu.membershippackages.subscription.Subscription;
import com.pregnancy.edu.membershippackages.subscription.SubscriptionRepository;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.common.PaymentProvider;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public PaymentServiceImpl(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> createPayment(Principal principal, HttpServletRequest req, Long subscriptionId, PaymentProvider provider) {
        try {
            if (principal == null) {
                throw new BadCredentialsException("Token cannot be found or trusted");
            }
            MyUser payer = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new AccountNotFoundException("Account not found"));

            Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ObjectNotFoundException("Subscription not found", subscriptionId.toString()));

            long userId = subscription.getUser().getId();
            MyUser user = userRepository.findById(userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

            long amount = subscription.getPayment().getAmount().longValue();

            if (provider == PaymentProvider.VNPAY)
                return createPaymentWithVNPay(amount, req);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> checkVNPayPayment(Principal principal, HttpServletRequest req, String vnp_TxnRef, String vnp_TransDate) throws IOException {
        return null;
    }

    private ResponseEntity<?> createPaymentWithVNPay(long amountParam, HttpServletRequest req) {
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_Command = "pay";

        long amount = amountParam * 100;

        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        //create param for vnpay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_BankCode", "NCB"); // default NCB for testing
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_OrderType", "other");   //optional
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = now.format(formatter);

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        ZonedDateTime expireDate = now.plusMinutes(15);

        String vnp_ExpireDate = expireDate.format(formatter);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);

        //encode all fields for export url
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();

        //create hash for checksum
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        ResponsePaymentDto responsePaymentDto = new ResponsePaymentDto();
        responsePaymentDto.setProvider(PaymentProvider.VNPAY);
        responsePaymentDto.setPaymentUrl(paymentUrl);
        System.out.println(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(responsePaymentDto);
    }

}
