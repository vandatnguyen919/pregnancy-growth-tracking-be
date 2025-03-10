package com.pregnancy.edu.membershippackages.order;

import com.pregnancy.edu.membershippackages.order.converter.OrderToOrderDtoConverter;
import com.pregnancy.edu.membershippackages.order.converter.OrderToOrderPaymentResponseConverter;
import com.pregnancy.edu.membershippackages.order.dto.CreateOrderRequest;
import com.pregnancy.edu.membershippackages.order.dto.OrderDto;
import com.pregnancy.edu.membershippackages.order.dto.OrderPaymentResponse;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.MyUserPrincipal;
import com.pregnancy.edu.myuser.UserService;
import com.pregnancy.edu.security.JwtProvider;
import com.pregnancy.edu.system.Result;
import com.pregnancy.edu.system.StatusCode;
import com.pregnancy.edu.system.common.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;
    private final OrderToOrderPaymentResponseConverter orderToOrderPaymentResponseConverter;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final OrderToOrderDtoConverter orderToOrderDtoConverter;

    public OrderController(
            OrderService orderService,
            OrderToOrderPaymentResponseConverter orderToOrderPaymentResponseConverter,
            UserService userService,
            JwtProvider jwtProvider, OrderToOrderDtoConverter orderToOrderDtoConverter) {
        this.orderService = orderService;
        this.orderToOrderPaymentResponseConverter = orderToOrderPaymentResponseConverter;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.orderToOrderDtoConverter = orderToOrderDtoConverter;
    }

    @GetMapping("/orders/my-orders")
    public Result findOrderByUserId(JwtAuthenticationToken jwtAuthenticationToken) {
        Jwt jwt = jwtAuthenticationToken.getToken();
        Long userId = jwt.getClaim("userId");
        List<Order> orders = orderService.findAllByUserId(userId);
        List<OrderDto> orderDtos = orders.stream().map(orderToOrderDtoConverter::convert).toList();
        return new Result(true, StatusCode.SUCCESS, "Find Orders Success", orderDtos);

    }

    @PostMapping("/payment/order")
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
    @GetMapping("/payment/check/vnpay")
    public Result checkVNPayPayment(
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef,
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        try {
            Jwt jwt = jwtAuthenticationToken.getToken();
            Long userId = jwt.getClaim("userId");

            Order order = orderService.checkVNPayPayment(vnp_TxnRef, userId);

            if ("COMPLETED".equals(order.getStatus())) {
                return processCompletedOrder(order, userId);
            } else if ("PENDING".equals(order.getStatus())) {
                return new Result(true, StatusCode.SUCCESS, "Payment Processing", Map.of(
                        "order", orderToOrderPaymentResponseConverter.convert(order)
                ));
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
    @GetMapping("/payment/check/momo")
    public Result checkMomoPayment(
            @RequestParam(name = "orderId") String orderId,
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        try {
            Jwt jwt = jwtAuthenticationToken.getToken();
            Long userId = jwt.getClaim("userId");

            Order completedOrder = orderService.checkMomoPayment(orderId, userId);
            if ("COMPLETED".equals(completedOrder.getStatus())) {
                return processCompletedOrder(completedOrder, userId);
            } else {
                return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed", null);
            }
        } catch (Exception e) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed: " + e.getMessage(), null);
        }
    }

    private Result processCompletedOrder(Order completedOrder, Long userId) {
        MyUser user = userService.findById(userId);

        user.setRole(Role.MEMBER.getDisplayName());
        userService.update(userId, user);

        MyUserPrincipal userPrincipal = new MyUserPrincipal(user);
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                userPrincipal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        String newToken = jwtProvider.createToken(newAuthentication);

        OrderPaymentResponse completedOrderResponse = orderToOrderPaymentResponseConverter.convert(completedOrder);

        return new Result(true, StatusCode.SUCCESS, "Payment Completed", Map.of(
                "order", completedOrderResponse,
                "token", newToken
        ));
    }
}