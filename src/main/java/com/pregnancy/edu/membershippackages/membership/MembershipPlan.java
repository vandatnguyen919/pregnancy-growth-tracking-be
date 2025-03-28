package com.pregnancy.edu.membershippackages.membership;

import com.pregnancy.edu.membershippackages.order.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class MembershipPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private Integer durationMonths;
    private boolean isActive;

    public Integer getDurationInDays() {
        return durationMonths * 30;
    }

    @OneToMany(mappedBy = "membershipPlan", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orders;
}
