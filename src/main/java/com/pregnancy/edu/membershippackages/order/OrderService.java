package com.pregnancy.edu.membershippackages.order;

import com.pregnancy.edu.client.payment.PaymentClient;
import com.pregnancy.edu.client.payment.dto.PaymentCreationResponse;
import com.pregnancy.edu.client.payment.dto.PaymentQueryResponse;
import com.pregnancy.edu.client.payment.utils.VNPayUtils;
import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.membershippackages.membership.MembershipPlanRepository;
import com.pregnancy.edu.membershippackages.order.dto.CreateOrderRequest;
import com.pregnancy.edu.membershippackages.order.exception.UnauthorizedException;
import com.pregnancy.edu.myuser.MyUser;
import com.pregnancy.edu.myuser.UserRepository;
import com.pregnancy.edu.system.common.PaymentProvider;
import com.pregnancy.edu.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {
     private final OrderRepository orderRepository;
     private final UserRepository userRepository;
     private final MembershipPlanRepository membershipPlanRepository;
     private final PaymentClient vnPayPaymentClient;
     private final PaymentClient momoPaymentClient;
     private final Map<String, PaymentClient> paymentClientMap;

     public OrderService(
             OrderRepository orderRepository,
             UserRepository userRepository,
             MembershipPlanRepository membershipPlanRepository,
             @Qualifier("vnPayPaymentClient") PaymentClient vnPayPaymentClient,
             @Qualifier("momoPaymentClient") PaymentClient momoPaymentClient) {
         this.orderRepository = orderRepository;
         this.userRepository = userRepository;
         this.membershipPlanRepository = membershipPlanRepository;
         this.vnPayPaymentClient = vnPayPaymentClient;
         this.momoPaymentClient = momoPaymentClient;

         this.paymentClientMap = new HashMap<>();
         this.paymentClientMap.put("VNPAY", vnPayPaymentClient);
         this.paymentClientMap.put("MOMO", momoPaymentClient);
     }

     public List<Order> findAllByUserId(Long userId) {
         return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
     }

     /**
      * Process order creation including payment initialization
      */
     public Map<String, Object> processOrder(CreateOrderRequest request) {

         MembershipPlan membershipPlan = membershipPlanRepository.findById(request.getMembershipPlanId())
                 .filter(MembershipPlan::isActive)
                 .orElseThrow(() -> new ObjectNotFoundException("Active membership plan", request.getMembershipPlanId()));

         Double amount = membershipPlan.getPrice();
         PaymentClient selectedClient = getPaymentClient(request.getProvider());

         Order initialOrder = createInitialOrder(
                 request.getUserId(),
                 request.getMembershipPlanId(),
                 amount,
                 request.getProvider()
         );

         PaymentCreationResponse paymentResponse = selectedClient.createPayment(amount.longValue());

         initialOrder.setTransactionId(paymentResponse.transactionId());
         orderRepository.save(initialOrder);

         return Map.of(
                 "order", initialOrder,
                 "paymentUrl", paymentResponse.paymentUrl()
         );
     }

     /**
      * Check payment status for VNPay with user authorization
      */
     public Order checkVNPayPayment(String vnp_TxnRef, Long authenticatedUserId) {
         // Check if the order belongs to the authenticated user
         Order order = orderRepository.findByTransactionId(vnp_TxnRef)
                 .orElseThrow(() -> new IllegalArgumentException("Order not found for transaction: " + vnp_TxnRef));

         verifyOrderOwnership(order, authenticatedUserId);

         PaymentQueryResponse paymentQueryResponse = vnPayPaymentClient.queryPayment(vnp_TxnRef);
         return completeOrder(vnp_TxnRef, paymentQueryResponse);
     }

     /**
      * Check payment status for Momo with user authorization
      */
     public Order checkMomoPayment(String orderId, Long authenticatedUserId) {
         // Check if the order belongs to the authenticated user
         Order order = orderRepository.findByTransactionId(orderId)
                 .orElseThrow(() -> new IllegalArgumentException("Order not found for transaction: " + orderId));

         verifyOrderOwnership(order, authenticatedUserId);

         PaymentQueryResponse paymentQueryResponse = momoPaymentClient.queryPayment(orderId);
         return completeOrder(orderId, paymentQueryResponse);
     }

     /**
      * Verify that the order belongs to the authenticated user
      */
     private void verifyOrderOwnership(Order order, Long authenticatedUserId) {
         if (!order.getUser().getId().equals(authenticatedUserId)) {
             throw new UnauthorizedException("You are not authorized to access this order");
         }
     }

     /**
      * Get appropriate payment client based on provider name
      */
     private PaymentClient getPaymentClient(String provider) {
         return paymentClientMap.getOrDefault(provider, vnPayPaymentClient); // Default to VNPay if provider is invalid
     }

     public Order createInitialOrder(Long userId, Long membershipPlanId, Double amount, String provider) {
         MyUser user = userRepository.findById(userId)
                 .orElseThrow(() -> new IllegalArgumentException("User not found"));

         MembershipPlan membershipPlan = membershipPlanRepository.findById(membershipPlanId)
                 .orElseThrow(() -> new IllegalArgumentException("Membership plan not found"));

         Order order = new Order();
         order.setUser(user);
         order.setMembershipPlan(membershipPlan);
         order.setAmount(amount);
         order.setProvider(provider);
         order.setCurrency("VND");
         order.setCreatedAt(LocalDateTime.now());
         order.setStatus("PENDING");

         return orderRepository.save(order);
     }

    public Order completeOrder(String transactionId, PaymentQueryResponse paymentResponse) {
        Order order = orderRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for transaction: " + transactionId));

        switch (paymentResponse.status()) {
            case COMPLETED:
                order.setStatus("COMPLETED");
                order.setTransactionDate(LocalDateTime.now());

                LocalDateTime startDate = LocalDateTime.now();
                order.setStartDate(startDate);

                int durationInDays = order.getMembershipPlan().getDurationInDays();
                order.setEndDate(startDate.plusDays(durationInDays));
                break;
            case PENDING:
            case PROCESSING:
                order.setStatus("PENDING");
                break;
            case FAILED:
                order.setStatus("FAILED");
                break;
            case REFUNDED:
                order.setStatus("REFUNDED");
                break;
            default:
                order.setStatus("UNKNOWN");
        }

        return orderRepository.save(order);
    }
}