package com.pregnancy.edu.client.payment.dto;

public record VNPayQueryRequest(
        String vnp_RequestId,
        String vnp_Version,
        String vnp_Command,
        String vnp_TmnCode,
        String vnp_TxnRef,
        String vnp_OrderInfo,
        String vnp_TransactionDate,
        String vnp_CreateDate,
        String vnp_IpAddr,
        String vnp_SecureHash
) { }
