package com.example.bankcards.exception;

public class NegativeAmountException extends BankException {
    public NegativeAmountException(String message) {
        super(message);
    }

    public NegativeAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
