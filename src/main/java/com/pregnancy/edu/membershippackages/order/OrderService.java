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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
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

     /**
      * Process order creation including payment initialization
      */
     public Map<String, Object> processOrder(CreateOrderRequest request) {
         PaymentClient selectedClient = getPaymentClient(request.getProvider());

         // amount lấy trong cột price bảng membership plan
         // find membership plan bằng membershipPlanId.
         // Nếu plan đó có và active thì lấy ko thì throw ObjectNotFoundException
         PaymentCreationResponse paymentResponse = selectedClient.createPayment(
                 request.getAmount().longValue());

         Order initialOrder = createInitialOrder(
                 request.getUserId(),
                 request.getMembershipPlanId(),
                 request.getAmount(),
                 paymentResponse.provider(),
                 paymentResponse.transactionId()
         );

         return Map.of(
                 "order", initialOrder,
                 "paymentUrl", paymentResponse.paymentUrl()
         );
     }

     /**
      * Check payment status for VNPay with user authorization
      */
     public Order checkVNPayPayment(String transactionId, Long authenticatedUserId) {
         // Check if the order belongs to the authenticated user
         Order order = orderRepository.findByTransactionId(transactionId)
                 .orElseThrow(() -> new IllegalArgumentException("Order not found for transaction: " + transactionId));

         verifyOrderOwnership(order, authenticatedUserId);

         PaymentQueryResponse paymentQueryResponse = vnPayPaymentClient.queryPayment(transactionId);
         return completeOrder(transactionId, paymentQueryResponse);
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

     public Order createInitialOrder(Long userId, Long membershipPlanId, Double amount, PaymentProvider provider, String transactionId) {
         MyUser user = userRepository.findById(userId)
                 .orElseThrow(() -> new IllegalArgumentException("User not found"));

         MembershipPlan membershipPlan = membershipPlanRepository.findById(membershipPlanId)
                 .orElseThrow(() -> new IllegalArgumentException("Membership plan not found"));

         Order order = new Order();
         order.setUser(user);
         order.setMembershipPlan(membershipPlan);
         order.setAmount(amount);
         order.setProvider(provider.name());
         order.setCurrency("VND");
         order.setTransactionId(transactionId);
         order.setCreatedAt(LocalDateTime.now());
         order.setStatus("PENDING");

         return orderRepository.save(order);
     }

     @Transactional
     public Order completeOrder(String transactionId, PaymentQueryResponse paymentResponse) {
         Order order = orderRepository.findByTransactionId(transactionId)
                 .orElseThrow(() -> new IllegalArgumentException("Order not found for transaction: " + transactionId));

         if (paymentResponse.code() == 0) {
             order.setStatus("COMPLETED");
             order.setTransactionDate(LocalDateTime.now());

             LocalDateTime startDate = LocalDateTime.now();
             order.setStartDate(startDate);

             int durationInDays = order.getMembershipPlan().getDurationInDays();
             order.setEndDate(startDate.plusDays(durationInDays));
         } else {
             order.setStatus("FAILED");
         }

         return orderRepository.save(order);
     }
}