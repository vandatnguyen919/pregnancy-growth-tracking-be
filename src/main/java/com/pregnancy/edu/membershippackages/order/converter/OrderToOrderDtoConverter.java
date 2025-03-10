package com.pregnancy.edu.membershippackages.order.converter;

import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.membershippackages.order.dto.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderToOrderDtoConverter implements Converter<Order, OrderDto> {
    @Override
    public OrderDto convert(Order source) {
        return new OrderDto(
                source.getId(),
                source.getAmount(),
                source.getProvider(),
                source.getCurrency(),
                source.getTransactionId(),
                formatDateTimeToString(source.getTransactionDate()),
                source.getStatus(),
                formatDateTimeToString(source.getCreatedAt()),
                formatDateTimeToString(source.getStartDate()),
                formatDateTimeToString(source.getEndDate()),
                source.getMembershipPlan() != null ? source.getMembershipPlan().getId() : null,
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