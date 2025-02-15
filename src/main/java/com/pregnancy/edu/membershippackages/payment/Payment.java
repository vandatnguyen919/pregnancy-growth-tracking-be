package com.pregnancy.edu.membershippackages.payment;

import com.pregnancy.edu.membershippackages.subscription.Subscription;
import com.pregnancy.edu.myuser.MyUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long subscriptionId;
    private Double amount;
    private String provider;
    private String currency;
    private String transactionId;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;

    @ManyToOne
    private MyUser user;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Subscription subscription;
}
