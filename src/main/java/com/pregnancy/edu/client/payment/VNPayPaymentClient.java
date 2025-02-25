package com.pregnancy.edu.client.payment;

import com.pregnancy.edu.client.payment.dto.CreatePaymentResponse;
import com.pregnancy.edu.client.payment.config.VNPayConfig;
import com.pregnancy.edu.client.payment.dto.VNPayQueryRequest;
import com.pregnancy.edu.client.payment.dto.VNPayQueryResponse;
import com.pregnancy.edu.system.common.EncryptionUtils;
import com.pregnancy.edu.system.common.PaymentProvider;
import com.pregnancy.edu.system.exception.PaymentException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Client for integrating with VNPay payment gateway.
 * Handles payment creation and transaction queries.
 */
@Slf4j
@Service
public class VNPayPaymentClient {
    // Common constants
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final int RANDOM_ID_LENGTH = 8;

    // Payment specific constants
    private static final String DEFAULT_BANK_CODE = "NCB";
    private static final String DEFAULT_LOCALE = "vn";
    private static final String DEFAULT_ORDER_TYPE = "other";
    private static final String VND_CURRENCY = "VND";
    private static final String PAYMENT_COMMAND = "pay";
    private static final int PAYMENT_EXPIRATION_MINUTES = 15;
    private static final int AMOUNT_MULTIPLIER = 100;

    // Query specific constants
    private static final String QUERY_COMMAND = "querydr";
    private static final String QUERY_VERSION = "2.1.0";

    private final RestClient restClient;

    public VNPayPaymentClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(VNPayConfig.vnp_ApiUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * Creates a payment request and returns a URL for the payment page.
     *
     * @param amount Amount to be paid
     * @param request HTTP request
     * @return Response containing payment URL
     */
    public CreatePaymentResponse createPayment(long amount, HttpServletRequest request) {
        String transactionRef = VNPayConfig.getRandomNumber(RANDOM_ID_LENGTH);
        String ipAddress = VNPayConfig.getIpAddress(request);
        long vnpayAmount = amount * AMOUNT_MULTIPLIER;

        // Create time-related values
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        String createDate = now.format(DATE_FORMATTER);
        String expireDate = now.plusMinutes(PAYMENT_EXPIRATION_MINUTES).format(DATE_FORMATTER);

        // Build payment parameters and URL
        Map<String, String> paymentParams = createPaymentParams(transactionRef, vnpayAmount, ipAddress,
                request, createDate, expireDate);
        String paymentUrl = buildPaymentUrl(paymentParams);

        return new CreatePaymentResponse(PaymentProvider.VNPAY, paymentUrl);
    }

    /**
     * Queries the status of a VNPay transaction.
     *
     * @param txnRef Transaction reference ID
     * @param transDate Transaction date
     * @param request HTTP request
     * @return Response containing transaction details
     * @throws PaymentException if query fails
     */
    public VNPayQueryResponse queryTransaction(String txnRef, String transDate, HttpServletRequest request) {
        try {
            VNPayQueryRequest queryRequest = buildQueryRequest(txnRef, transDate, request);

            return restClient.post()
                    .body(queryRequest)
                    .retrieve()
                    .body(VNPayQueryResponse.class);
        } catch (RestClientException e) {
            log.error("Failed to query VNPay transaction: {}", txnRef, e);
            throw new PaymentException("Failed to query VNPay transaction", e);
        }
    }

    /**
     * Builds a VNPay query request with secure hash.
     */
    private VNPayQueryRequest buildQueryRequest(String txnRef, String transDate, HttpServletRequest request) {
        String requestId = VNPayConfig.getRandomNumber(RANDOM_ID_LENGTH);
        String createDate = ZonedDateTime.now(VIETNAM_ZONE).format(DATE_FORMATTER);
        String ipAddr = VNPayConfig.getIpAddress(request);
        String orderInfo = "Kiem tra ket qua GD OrderId:" + txnRef;

        // Using a record-based approach with a builder pattern
        VNPayQueryRequest.VNPayQueryRequestBuilder builder = VNPayQueryRequest.builder()
                .vnp_RequestId(requestId)
                .vnp_Version(QUERY_VERSION)
                .vnp_Command(QUERY_COMMAND)
                .vnp_TmnCode(VNPayConfig.vnp_TmnCode)
                .vnp_TxnRef(txnRef)
                .vnp_OrderInfo(orderInfo)
                .vnp_TransactionDate(transDate)
                .vnp_CreateDate(createDate)
                .vnp_IpAddr(ipAddr);

        // Generate secure hash
        String hashData = String.join("|",
                requestId, QUERY_VERSION, QUERY_COMMAND, VNPayConfig.vnp_TmnCode,
                txnRef, transDate, createDate, ipAddr, orderInfo
        );

        String secureHash = EncryptionUtils.hmacSHA512(VNPayConfig.secretKey, hashData);
        builder.vnp_SecureHash(secureHash);

        return builder.build();
    }

    /**
     * Creates payment parameters map.
     */
    private Map<String, String> createPaymentParams(String transactionRef, long amount, String ipAddress,
                                                    HttpServletRequest request, String createDate, String expireDate) {
        Map<String, String> params = new HashMap<>();

        // Add all required parameters
        params.put("vnp_Version", VNPayConfig.vnp_Version);
        params.put("vnp_Command", PAYMENT_COMMAND);
        params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_BankCode", DEFAULT_BANK_CODE);
        params.put("vnp_CurrCode", VND_CURRENCY);
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_OrderType", DEFAULT_ORDER_TYPE);
        params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        params.put("vnp_TxnRef", transactionRef);
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + transactionRef);

        // Handle locale - use request parameter if available or default
        String locale = request.getParameter("language");
        params.put("vnp_Locale", (locale != null && !locale.isEmpty()) ? locale : DEFAULT_LOCALE);

        return params;
    }

    /**
     * Builds payment URL with secure hash.
     */
    private String buildPaymentUrl(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        boolean isFirst = true;

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                if (!isFirst) {
                    hashData.append('&');
                    query.append('&');
                }

                String encodedValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII);
                String encodedName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII);

                hashData.append(fieldName).append('=').append(encodedValue);
                query.append(encodedName).append('=').append(encodedValue);

                isFirst = false;
            }
        }

        // Generate and append secure hash
        String secureHash = EncryptionUtils.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return VNPayConfig.vnp_PayUrl + "?" + query;
    }
}