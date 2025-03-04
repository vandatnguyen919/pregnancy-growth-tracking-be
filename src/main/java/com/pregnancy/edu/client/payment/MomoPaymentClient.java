package com.pregnancy.edu.client.payment;

import com.pregnancy.edu.client.payment.dto.*;
import com.pregnancy.edu.client.payment.utils.EncryptionUtils;
import com.pregnancy.edu.system.common.PaymentProvider;
import com.pregnancy.edu.system.exception.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service("momoPaymentClient")
public class MomoPaymentClient implements PaymentClient {

    @Value("${momo.partnerCode}")
    private String momoPartnerCode;

    @Value("${momo.accessKey}")
    private String momoAccessKey;

    @Value("${momo.secretKey}")
    private String momoSecretKey;

    @Value("${momo.requestType}")
    private String momoRequestType;

    @Value("${momo.redirectUrl}")
    private String momoRedirectUrl;

    @Value("${momo.ipnUrl}")
    private String momoIpnUrl;

    @Value("${momo.createUri}")
    private String momoCreateUri;

    @Value("${momo.queryUri}")
    private String momoQueryUri;

    private final RestClient restClient;

    public MomoPaymentClient(
            RestClient.Builder restClientBuilder,
            @Value("${momo.baseUrl}") String momoBaseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(momoBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public PaymentCreationResponse createPayment(long amount) {
        // MoMo parameters
        String orderInfo = "PregnaJoy - Pay with MoMo";
        String extraData = "";
        String orderId = momoPartnerCode + System.currentTimeMillis();

        // Create the raw data for the signature
        String rawData = generateRawHashData(
                momoAccessKey,
                amount,
                extraData,
                momoIpnUrl,
                orderId,
                orderInfo,
                momoPartnerCode,
                momoRedirectUrl,
                orderId,
                momoRequestType
        );

        // Calculate the HMAC SHA-256 signature
        String signature = EncryptionUtils.hmacSHA256(momoSecretKey, rawData);
        MomoPaymentRequest momoPaymentRequest = new MomoPaymentRequest(
                momoPartnerCode, momoAccessKey, orderId, amount, orderId, orderInfo,
                15, momoRedirectUrl, momoIpnUrl, extraData, momoRequestType, signature, "en"
        );

        try {
            MomoPaymentResponse response = restClient.post()
                    .uri(momoCreateUri)
                    .body(momoPaymentRequest)
                    .retrieve()
                    .body(MomoPaymentResponse.class);

            assert response != null;
            String paymentUrl = response.payUrl();
            return new PaymentCreationResponse(PaymentProvider.MOMO, orderId, paymentUrl);
        } catch (RestClientException e) {
            log.error("Failed to create Momo payment transaction: {}", orderId, e);
            throw new PaymentException("Failed to create Momo payment transaction", e);
        }
    }

    @Override
    public PaymentQueryResponse queryPayment(String orderId) {
        String rawData = generateRawHashDataForQuery(momoAccessKey, orderId, momoPartnerCode, orderId);

        // Calculate the HMAC SHA-256 signature
        String signature = EncryptionUtils.hmacSHA256(momoSecretKey, rawData);

        MomoQueryRequest momoQueryRequest = new MomoQueryRequest(momoPartnerCode, orderId, orderId, "en", signature);

        try {
            MomoQueryResponse momoQueryResponse = restClient.post()
                    .uri(momoQueryUri)
                    .body(momoQueryRequest)
                    .retrieve()
                    .body(MomoQueryResponse.class);
            return new PaymentQueryResponse(momoQueryResponse.resultCode(), momoQueryResponse.message());
        } catch (RestClientException e) {
            log.error("Failed to query Momo transaction: {}", orderId, e);
            throw new PaymentException("Failed to query Momo transaction", e);
        }
    }

    private String generateRawHashDataForQuery(String accessKey, String orderId, String partnerCode, String requestId) {
        return "accessKey=" + accessKey + "&orderId=" + orderId + "&partnerCode=" + partnerCode + "&requestId=" + requestId;
    }

    private String generateRawHashData(String accessKey, long amount, String extraData, String ipnUrl,
                                       String orderId, String orderInfo, String partnerCode,
                                       String redirectUrl, String requestId, String requestType) {
        return "accessKey=" + accessKey + "&amount=" + amount + "&extraData=" + extraData +
                "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo=" + orderInfo +
                "&partnerCode=" + partnerCode + "&redirectUrl=" + redirectUrl +
                "&requestId=" + requestId + "&requestType=" + requestType;
    }
}
