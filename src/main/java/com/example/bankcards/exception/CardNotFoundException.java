package com.example.bankcards.exception;

public class CardNotFoundException extends BankException {
    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 