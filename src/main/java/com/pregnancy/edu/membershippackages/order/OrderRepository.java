package com.pregnancy.edu.membershippackages.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByTransactionId(String transactionId);

    Page<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Order> findByUserIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long userId, LocalDateTime endDate, Pageable pageable);
    Page<Order> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(Long userId, LocalDateTime startDate, Pageable pageable);
    Page<Order> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, Pageable pageable, LocalDateTime startDate, LocalDateTime endDate);

    Page<Order> findByUserIdAndStatusAndStartDateBeforeAndEndDateAfterOrderByEndDateDesc(
            Long userId, String status, LocalDateTime beforeDate, LocalDateTime afterDate, Pageable pageable);
}