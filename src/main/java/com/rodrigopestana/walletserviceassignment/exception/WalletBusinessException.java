package com.rodrigopestana.walletserviceassignment.exception;

public class WalletBusinessException extends RuntimeException {
    private final String exceptionMessage;

    public WalletBusinessException(String exceptionMessage, String message) {
        super(message);
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
