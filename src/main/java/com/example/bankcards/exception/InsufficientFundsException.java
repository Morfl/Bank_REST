package com.example.bankcards.exception;

public class InsufficientFundsException extends BankException {
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
} 