package com.example.bankcards.exception;

public class CardNotBelongUserException extends BankException {
    public CardNotBelongUserException(String message) {
        super(message);
    }

    public CardNotBelongUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
