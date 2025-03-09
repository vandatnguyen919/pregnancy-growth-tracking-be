package com.pregnancy.edu.membershippackages.order.converter;

import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.membershippackages.order.dto.OrderDto;
import com.pregnancy.edu.membershippackages.order.dto.OrderPaymentResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderToOrderPaymentResponseConverter implements Converter<Order, OrderPaymentResponse> {
    @Override
    public OrderPaymentResponse convert(Order source) {
        return new OrderPaymentResponse(
                source.getId(),
                source.getAmount(),
                source.getProvider(),
                source.getCurrency(),
                source.getTransactionId(),
                formatDateTimeToString(source.getTransactionDate()),
                source.getMembershipPlan() != null ? source.getMembershipPlan().getName() : null,
                "PENDING",
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
