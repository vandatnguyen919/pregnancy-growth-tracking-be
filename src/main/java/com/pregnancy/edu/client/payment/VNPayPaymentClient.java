package com.pregnancy.edu.client.payment;

import com.pregnancy.edu.client.payment.dto.PaymentCreationResponse;
import com.pregnancy.edu.client.payment.dto.PaymentQueryResponse;
import com.pregnancy.edu.client.payment.dto.VNPayQueryRequest;
import com.pregnancy.edu.client.payment.dto.VNPayQueryResponse;
import com.pregnancy.edu.client.payment.utils.EncryptionUtils;
import com.pregnancy.edu.client.payment.utils.VNPayUtils;
import com.pregnancy.edu.system.common.PaymentProvider;
import com.pregnancy.edu.system.exception.PaymentException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
@Service("vnPayPaymentClient")
public class VNPayPaymentClient implements PaymentClient{

    @Value("${vnp.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnp.returnUrl}")
    private String vnp_ReturnUrl;

    @Value("${vnp.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnp.secretKey}")
    private String secretKey;

    @Value("${vnp.version}")
    private String vnp_Version;

    @Value("${vnp.queryUri}")
    private String vnp_QueryUri;

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

    public VNPayPaymentClient(
            RestClient.Builder restClientBuilder,
            @Value("${vnp.baseUrl}") String vnp_BaseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(vnp_BaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public PaymentCreationResponse createPayment(long amount) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();

        String transactionRef = VNPayUtils.getRandomNumber(RANDOM_ID_LENGTH);
        String ipAddress = VNPayUtils.getIpAddress(request);
        long vnpayAmount = amount * AMOUNT_MULTIPLIER;

        // Create time-related values
        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        String createDate = now.format(DATE_FORMATTER);
        String expireDate = now.plusMinutes(PAYMENT_EXPIRATION_MINUTES).format(DATE_FORMATTER);

        // Build payment parameters and URL
        Map<String, String> paymentParams = createPaymentParams(transactionRef, vnpayAmount, ipAddress,
                request, createDate, expireDate);
        String paymentUrl = buildPaymentUrl(paymentParams);

        return new PaymentCreationResponse(PaymentProvider.VNPAY, transactionRef, paymentUrl);
    }

    @Override
    public PaymentQueryResponse queryPayment(String transactionId) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();

        ZonedDateTime now = ZonedDateTime.now(VIETNAM_ZONE);
        String createDate = now.format(DATE_FORMATTER);

        try {
            VNPayQueryRequest queryRequest = buildQueryRequest(transactionId, createDate, request);

            VNPayQueryResponse vnPayQueryResponse = restClient.post()
                    .uri(vnp_QueryUri)
                    .body(queryRequest)
                    .retrieve()
                    .body(VNPayQueryResponse.class);
            return new PaymentQueryResponse(Integer.parseInt(vnPayQueryResponse.vnpResponseCode()), vnPayQueryResponse.vnpMessage());
        } catch (RestClientException e) {
            log.error("Failed to query VNPay transaction: {}", transactionId, e);
            throw new PaymentException("Failed to query VNPay transaction", e);
        }
    }

    /**
     * Builds a VNPay query request with secure hash.
     */
    private VNPayQueryRequest buildQueryRequest(String txnRef, String transDate, HttpServletRequest request) {
        String requestId = VNPayUtils.getRandomNumber(RANDOM_ID_LENGTH);
        String createDate = ZonedDateTime.now(VIETNAM_ZONE).format(DATE_FORMATTER);
        String ipAddr = VNPayUtils.getIpAddress(request);
        String orderInfo = "Kiem tra ket qua GD OrderId:" + txnRef;

        // Generate secure hash
        String hashData = String.join("|",
                requestId, QUERY_VERSION, QUERY_COMMAND, vnp_TmnCode,
                txnRef, transDate, createDate, ipAddr, orderInfo
        );

        String secureHash = EncryptionUtils.hmacSHA512(secretKey, hashData);

        return new VNPayQueryRequest(
                requestId,
                QUERY_VERSION,
                QUERY_COMMAND,
                vnp_TmnCode,
                txnRef,
                orderInfo,
                transDate,
                createDate,
                ipAddr,
                secureHash
        );
    }

    /**
     * Creates payment parameters map.
     */
    private Map<String, String> createPaymentParams(String transactionRef, long amount, String ipAddress,
                                                    HttpServletRequest request, String createDate, String expireDate) {
        Map<String, String> params = new HashMap<>();

        // Add all required parameters
        params.put("vnp_OrderInfo", "Thanh toan don hang:" + transactionRef);
        params.put("vnp_TxnRef", transactionRef);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_Version", vnp_Version);
        params.put("vnp_Command", PAYMENT_COMMAND);
        params.put("vnp_TmnCode", vnp_TmnCode);
        params.put("vnp_BankCode", DEFAULT_BANK_CODE);
        params.put("vnp_CurrCode", VND_CURRENCY);
        params.put("vnp_IpAddr", ipAddress);
        params.put("vnp_OrderType", DEFAULT_ORDER_TYPE);
        params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);

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
        String secureHash = EncryptionUtils.hmacSHA512(secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        return vnp_PayUrl + "?" + query;
    }
}