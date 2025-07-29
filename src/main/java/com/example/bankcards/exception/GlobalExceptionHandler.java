package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//ne robit so swaggerom idk

//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(CardNotFoundException.class)
//    public ResponseEntity<Object> handleCardNotFoundException(
//            CardNotFoundException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(UserNotFoundException.class)
//    public ResponseEntity<Object> handleUserNotFoundException(
//            UserNotFoundException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(InsufficientFundsException.class)
//    public ResponseEntity<Object> handleInsufficientFundsException(
//            InsufficientFundsException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(BankException.class)
//    public ResponseEntity<Object> handleBankException(
//            BankException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllUncaughtException(
//            Exception ex, WebRequest request) throws Exception {
//        String path = request.getDescription(false);
//        if (path.contains("/v3/api-docs") || path.contains("/swagger-ui")) {
//            throw ex;
//        }
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", "Произошла непредвиденная ошибка");
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @ExceptionHandler(CardNotBelongUserException.class)
//    public ResponseEntity<Object> handleCardNotBelongUserException(
//            CardNotBelongUserException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.FORBIDDEN.value());
//
//        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(NegativeAmountException.class)
//    public ResponseEntity<Object> handleNegativeAmountException(
//            NegativeAmountException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.BAD_REQUEST.value());
//
//        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(CardOperationException.class)
//    public ResponseEntity<Object> handleCardOperationException(
//            CardOperationException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(NoCardsException.class)
//    public ResponseEntity<Object> handleNoCardsException(
//            NoCardsException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.NOT_FOUND.value());
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(DecryptingCardException.class)
//    public ResponseEntity<Object> handleDecryptingCardException(
//            DecryptingCardException ex, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", ex.getMessage());
//        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
//
//        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//}