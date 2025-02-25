package com.pregnancy.edu.membershippackages.payment.converter;

import com.pregnancy.edu.membershippackages.payment.Payment;
import com.pregnancy.edu.membershippackages.payment.dto.PaymentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentToPaymentDtoConverter implements Converter<Payment, PaymentDto> {
    @Override
    public PaymentDto convert(Payment source) {
        return new PaymentDto(
                source.getId(),
                source.getAmount(),
                source.getProvider(),
                source.getCurrency(),
                source.getTransactionId(),
                formatDateTimeToString(source.getTransactionDate()),
                source.getSubscription() != null ? source.getSubscription().getId() : null,
                source.getUser() != null ? source.getUser().getId() : null
        );
    }

    private String formatDateTimeToString(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}