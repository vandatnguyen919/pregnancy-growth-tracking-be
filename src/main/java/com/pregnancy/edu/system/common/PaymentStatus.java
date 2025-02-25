package com.pregnancy.edu.system.common;

public enum PaymentStatus {
    PENDING("Pending"),
    SUCCESS("Success"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
