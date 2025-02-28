package com.pregnancy.edu.client.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VNPayQueryResponse(
        @JsonProperty("vnp_ResponseCode") String vnpResponseCode,
        @JsonProperty("vnp_Message") String vnpMessage
) {
}
