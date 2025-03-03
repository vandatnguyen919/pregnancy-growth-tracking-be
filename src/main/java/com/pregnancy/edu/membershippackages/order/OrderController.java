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
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class OrderController {

    private final PaymentClient vnPayPaymentClient;

    private final PaymentClient momoPaymentClient;

    private final OrderService orderService;

    private final OrderToOrderPaymentResponseConverter orderToOrderPaymentResponseConverter;

    public OrderController(
            @Qualifier("vnPayPaymentClient") PaymentClient vnPayPaymentClient,
            @Qualifier("momoPaymentClient") PaymentClient momoPaymentClient, OrderService orderService,
            OrderToOrderPaymentResponseConverter orderToOrderPaymentResponseConverter) {
        this.vnPayPaymentClient = vnPayPaymentClient;
        this.momoPaymentClient = momoPaymentClient;
        this.orderService = orderService;
        this.orderToOrderPaymentResponseConverter = orderToOrderPaymentResponseConverter;
    }

    @PostMapping("/callback/vnpay")
    public Result handleVNPayCallback(
            @RequestParam(name = "vnp_TxnRef") String transactionId,
            @RequestParam(name = "vnp_ResponseCode") String responseCode,
            @RequestParam(name = "vnp_Amount") String amount
    ) {
        PaymentQueryResponse paymentResponse = vnPayPaymentClient.queryPayment(transactionId);

        if (paymentResponse.code() == 0) {
            Order completedOrder = orderService.completeOrder(transactionId, paymentResponse);
            OrderPaymentResponse completedOrderResponse = orderToOrderPaymentResponseConverter.convert(completedOrder);
            return new Result(true, StatusCode.SUCCESS, "Payment Completed", completedOrderResponse);
        } else {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed", paymentResponse);
        }
    }

    @PostMapping("/callback/momo")
    public Result handleMomoCallback(
            @RequestParam(name = "orderId") String orderId,
            @RequestParam(name = "resultCode") String resultCode
    ) {
        PaymentQueryResponse paymentResponse = momoPaymentClient.queryPayment(orderId);

        if (paymentResponse.code() == 0) {
            Order completedOrder = orderService.completeOrder(orderId, paymentResponse);
            OrderPaymentResponse completedOrderResponse = orderToOrderPaymentResponseConverter.convert(completedOrder);
            return new Result(true, StatusCode.SUCCESS, "Payment Completed", completedOrderResponse);
        } else {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Failed", paymentResponse);
        }
    }

    @PostMapping("/order")
    public Result createOrder(@RequestBody CreateOrderRequest request) {
        String transactionId = request.getTransactionId();
        if (transactionId == null || transactionId.isEmpty()) {
            transactionId = VNPayUtils.getRandomNumber(8);
        }

        PaymentCreationResponse paymentResponse;

        if ("VNPAY".equals(request.getProvider())) {
            paymentResponse = vnPayPaymentClient.createPaymentWithTransactionId(
                    request.getAmount().longValue(), transactionId);
        } else {
            paymentResponse = momoPaymentClient.createPaymentWithTransactionId(
                    request.getAmount().longValue(), transactionId);
        }

        // Create the order with the SAME transaction ID used for payment
        Order initialOrder = orderService.createInitialOrder(
                request.getUserId(),
                request.getMembershipPlanId(),
                request.getAmount(),
                paymentResponse.provider(),
                transactionId  // Use the generated/provided transaction ID
        );

        OrderPaymentResponse initialOrderResponse = orderToOrderPaymentResponseConverter.convert(initialOrder);

        return new Result(true, StatusCode.SUCCESS, "Order Created", Map.of(
                "order", initialOrderResponse,
                "paymentUrl", paymentResponse.paymentUrl()
        ));
    }
    /**
     * Creates a VNPay payment URL.
     * Example URL: GET /api/v1/payment/create/vnpay?amount=100000
     *
     * @param amount  the amount to be paid
     * @return a Result object containing the payment URL and VNPay provider
     */
    @GetMapping("/create/vnpay")
    public Result createVNPayPayment(
            @Param(value = "amount") Long amount
    ) {
        PaymentCreationResponse paymentCreationResponse = vnPayPaymentClient.createPayment(amount);
        return new Result(true, StatusCode.SUCCESS, "Payment Url Created Success", paymentCreationResponse);
    }

    /**
     * Creates a Momo payment URL.
     * Example URL: GET /api/v1/payment/create/momo?amount=100000
     *
     * @param amount  the amount to be paid
     * @return a Result object containing the payment URL and Momo provider
     */
    @GetMapping("/create/momo")
    public Result createMomoPayment(
            @Param(value = "amount") Long amount
    ) {
        PaymentCreationResponse paymentCreationResponse = momoPaymentClient.createPayment(amount);
        return new Result(true, StatusCode.SUCCESS, "Payment Url Created Success", paymentCreationResponse);
    }

    /**
     * Checks the status of a VNPay payment.
     * Example URL: GET /api/v1/payment/check/vnpay?vnp_TxnRef=59796198
     *
     * @param vnp_TxnRef    the transaction reference number
     * @return a Result object containing the status of the payment check
     */
    @GetMapping("/check/vnpay")
    public Result checkVNPayPayment(
            @RequestParam(name = "vnp_TxnRef") String vnp_TxnRef
    ) {
        PaymentQueryResponse paymentQueryResponse = vnPayPaymentClient.queryPayment(vnp_TxnRef);
        if (paymentQueryResponse.code() != 0) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Checked Failed", paymentQueryResponse);
        }
        return new Result(true, StatusCode.SUCCESS, "Payment Checked Success", paymentQueryResponse);
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
            @RequestParam(name = "orderId") String orderId
    ) {
        PaymentQueryResponse paymentQueryResponse = momoPaymentClient.queryPayment(orderId);
        if (paymentQueryResponse.code() != 0) {
            return new Result(false, StatusCode.INVALID_ARGUMENT, "Payment Checked Failed", paymentQueryResponse);
        }
        return new Result(true, StatusCode.SUCCESS, "Payment Checked Success", paymentQueryResponse);
    }

}