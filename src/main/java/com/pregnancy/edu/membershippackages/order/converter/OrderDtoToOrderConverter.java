package com.pregnancy.edu.membershippackages.order.converter;

import com.pregnancy.edu.membershippackages.order.Order;
import com.pregnancy.edu.membershippackages.order.dto.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderDtoToOrderConverter implements Converter<OrderDto, Order> {
    @Override
    public Order convert(OrderDto source) {
        Order order = new Order();
        order.setId(source.id());
        order.setAmount(source.amount());
        order.setProvider(source.provider());
        order.setCurrency(source.currency());
        order.setTransactionId(source.transactionId());
        order.setTransactionDate(formatStringToDateTime(String.valueOf(source.transactionDate())));
        return order;
    }

    private LocalDateTime formatStringToDateTime(String dateTime) {
        if(dateTime == null || dateTime.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }
}