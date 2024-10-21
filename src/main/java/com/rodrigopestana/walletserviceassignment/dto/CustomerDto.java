package com.rodrigopestana.walletserviceassignment.dto;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public class CustomerDto {
    @NonNull
    private Long customerId;
    @NonNull
    private BigDecimal amount;
    private String timestamp;

    public CustomerDto() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}