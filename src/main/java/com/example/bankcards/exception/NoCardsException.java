package com.example.bankcards.exception;

public class NoCardsException extends BankException {

    public NoCardsException(String message) {
        super(message);
    }

    public NoCardsException(String message, Throwable cause) {
        super(message, cause);
    }
}
