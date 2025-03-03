package com.pregnancy.edu.membershippackages.order;

import com.pregnancy.edu.membershippackages.membership.MembershipPlan;
import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_order")
@Data
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String provider;
    private String currency;
    private String transactionId;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    @ManyToOne
    private MyUser user;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private MembershipPlan membershipPlan;
}
