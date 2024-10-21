package com.rodrigopestana.walletserviceassignment.dto;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public class TransferDto {
    @NonNull
    private Long customerSenderId;
    @NonNull
    private Long customerReceiverId;
    @NonNull
    private BigDecimal amount;

    public TransferDto() {
    }

    public Long getCustomerSenderId() {
        return customerSenderId;
    }

    public void setCustomerSenderId(Long customerSenderId) {
        this.customerSenderId = customerSenderId;
    }

    public Long getCustomerReceiverId() {
        return customerReceiverId;
    }

    public void setCustomerReceiverId(Long customerReceiverId) {
        this.customerReceiverId = customerReceiverId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
