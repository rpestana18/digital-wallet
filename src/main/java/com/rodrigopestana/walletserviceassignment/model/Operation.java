package com.rodrigopestana.walletserviceassignment.model;

public enum Operation {

    RETRIEVE_BALANCE(1L, "Retrieve Balance"),
    DEPOSIT(2L, "Deposit"),
    WITHDRAW(3L, "Withdraw"),
    TRANSFER_SENDER(4L, "Transfer Send"),
    TRANSFER_RECEIVER(5L, "Transfer Receive"),
    RETRIEVE_HISTORICAL_BALANCE(6L, "Retrieve Historical Balance"),
    CREATE_WALLET(7L, "Wallet Creation");

    private Long id;
    private String name;

    Operation(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

}
