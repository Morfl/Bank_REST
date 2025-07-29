package com.example.bankcards.exception;

public class DecryptingCardException extends BankException {

    public DecryptingCardException(String message) {
        super(message);
    }

    public DecryptingCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
