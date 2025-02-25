package com.pregnancy.edu.membershippackages.payment.converter;

import com.pregnancy.edu.membershippackages.subscription.SubscriptionRepository;
import com.pregnancy.edu.membershippackages.payment.Payment;
import com.pregnancy.edu.membershippackages.payment.dto.PaymentDto;
import com.pregnancy.edu.myuser.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PaymentDtoToPaymentConverter implements Converter<PaymentDto, Payment> {
    @Override
    public Payment convert(PaymentDto source) {
        Payment payment = new Payment();
        payment.setId(source.id());
        payment.setAmount(source.amount());
        payment.setProvider(source.provider());
        payment.setCurrency(source.currency());
        payment.setTransactionId(source.transactionId());
        payment.setTransactionDate(formatStringToDateTime(String.valueOf(source.transactionDate())));
        return payment;
    }

    private LocalDateTime formatStringToDateTime(String dateTime) {
        if(dateTime == null || dateTime.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }
}