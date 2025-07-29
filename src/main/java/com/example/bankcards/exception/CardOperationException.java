package com.example.bankcards.exception;

public class CardOperationException extends BankException {
    public CardOperationException(String message) {
        super(message);
    }

    public CardOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
