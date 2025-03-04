package com.pregnancy.edu.membershippackages.order;

import com.pregnancy.edu.client.payment.MomoPaymentClient;
import com.pregnancy.edu.client.payment.PaymentClient;
import com.pregnancy.edu.client.payment.VNPayPaymentClient;
import com.pregnancy.edu.client.payment.dto.PaymentCreationResponse;
import com.pregnancy.edu.client.payment.dto.MomoQueryResponse;
import com.pregnancy.edu.client.payment.dto.PaymentQueryResponse;
import com.pregnancy.edu.client.payment.dto.VNPayQueryResponse;
import com.pregnancy.edu.client.payment.utils.VNPayUtils;
import com.pregnancy.edu.membershippackages.order.converter.OrderDtoToOrderConverter;
import com.pregnancy.edu.membershippackages.order.converter.OrderToOrderDtoConverter;
import com.pregnancy.edu.membershippackages.order.converter.OrderToOrderPaymentResponseConverter;
import com.pregnancy.edu.membershippackages.order.dto.CreateOrderRequest;
import com.pregnancy.edu.membershippackages.order.dto.OrderDto;
import com.pregnancy.edu.membershippackages.order.dto.OrderPaymentResponse;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class OrderController {

    private final OrderService orderService;

    private final OrderToOrderPaymentResponseConverter orderToOrderPaymentResponseConverter;

    public OrderController(
            OrderService orderService,
            OrderToOrderPaymentResponseConverter orderToOrderPaymentResponseConverter) {
        this.orderService = orderService;
        this.orderToOrderPaymentResponseConverter = orderToOrderPaymentResponseConverter;
    }

    @PostMapping("/order")
    public Result createOrder(@RequestBody CreateOrderRequest request, JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");

        request.setUserId(userId);

        Map<String, Object> orderResult = orderService.processOrder(request);

        Order initialOrder = (Order) orderResult.get("order");
        String paymentUrl = (String) orderResult.get("paymentUrl");

        OrderPaymentResponse initialOrderResponse = orderToOrderPaymentResponseConverter.convert(initialOrder);

        return new Result(true, StatusCode.SUCCESS, "Order Created", Map.of(
                "order", initialOrderResponse,
                "paymentUrl", paymentUrl
        ));
    }

    /**
     * Checks the status of a VNPay payment.
     * Example URL: GET /api/v1/payment/check/vnpay?vnp_TxnRef=59796198
     *
     * @param vnp_TxnRef the transaction reference number
     * @return a Result object containing the status of the payment check
     */
    @GetMapping("/check/vnpay")
    public Result checkVNPayPayment(
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        try {
            Jwt jwt = jwtAuthenticationToken.getToken();
            Long userId = jwt.getClaim("userId");

            Order completedOrder = orderService.checkVNPayPayment(vnp_TxnRef, userId);
            if ("COMPLETED".equals(completedOrder.getStatus())) {
                OrderPaymentResponse completedOrderResponse = orderToOrderPaymentResponseConverter.convert(completedOrder);
                return new Result(true, StatusCode.SUCCESS, "Payment Completed", completedOrderResponse);
            } else {
                return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed", null);
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed: " + e.getMessage(), null);
        }
    }

    /**
     * Checks the status of a Momo payment.
     * Example URL: GET /api/v1/payment/check/momo?orderId=MOMO1740729694232
     *
     * @param orderId the order ID to be checked
     * @return a Result object containing the status of the payment check
     */
    @GetMapping("/check/momo")
    public Result checkMomoPayment(
            @RequestParam(name = "orderId") String orderId,
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        try {
            // Extract userId from JWT token
            Jwt jwt = jwtAuthenticationToken.getToken();
            Long userId = jwt.getClaim("userId");

            Order completedOrder = orderService.checkMomoPayment(orderId, userId);
            if ("COMPLETED".equals(completedOrder.getStatus())) {
                OrderPaymentResponse completedOrderResponse = orderToOrderPaymentResponseConverter.convert(completedOrder);
                return new Result(true, StatusCode.SUCCESS, "Payment Completed", completedOrderResponse);
            } else {
                return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed", null);
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed: " + e.getMessage(), null);
        }
    }
}