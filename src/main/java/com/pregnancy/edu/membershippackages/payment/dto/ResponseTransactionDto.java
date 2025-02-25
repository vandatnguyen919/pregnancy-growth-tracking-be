package com.pregnancy.edu.membershippackages.payment.dto;

import com.pregnancy.edu.membershippackages.subscription.Subscription;
import com.pregnancy.edu.membershippackages.subscription.dto.SubscriptionDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ResponseTransactionDto {
    private int id;

    private Double moneyAmount;

    private String provider;

    private LocalDateTime transactionTime;

    private String transactionDate;

    private String transactionId;

    private SubscriptionDto subscription;
}