package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardOperationsServiceTest {
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private CardOperationsService cardOperationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deposit_success() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.ZERO);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        cardOperationsService.deposit(1L, BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, card.getBalance());
        verify(cardRepository).save(card);
    }

    @Test
    void deposit_negativeAmount_throwsException() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.ZERO);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        assertThrows(NegativeAmountException.class, () -> cardOperationsService.deposit(1L, BigDecimal.valueOf(-5)));
    }

    @Test
    void deposit_cardNotFound_throwsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardOperationsService.deposit(1L, BigDecimal.TEN));
    }

    @Test
    void transfer_success() {
        Card from = new Card();
        from.setId(1L);
        from.setBalance(new BigDecimal("100.00"));
        Card to = new Card();
        to.setId(2L);
        to.setBalance(new BigDecimal("10.00"));
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 2L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(to));
        cardOperationsService.transfer(1L, 2L, new BigDecimal("50.00"), "mail");
        assertEquals(new BigDecimal("50.00"), from.getBalance());
        assertEquals(new BigDecimal("60.00"), to.getBalance());
        verify(cardRepository).save(from);
        verify(cardRepository).save(to);
    }

    @Test
    void transfer_fromCardNotBelongUser() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(false);
        assertThrows(CardNotBelongUserException.class, () -> cardOperationsService.transfer(1L, 2L, BigDecimal.TEN, "mail"));
    }

    @Test
    void transfer_toCardNotBelongUser() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 2L)).thenReturn(false);
        assertThrows(CardNotBelongUserException.class, () -> cardOperationsService.transfer(1L, 2L, BigDecimal.TEN, "mail"));
    }

    @Test
    void transfer_fromCardNotFound() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 2L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardOperationsService.transfer(1L, 2L, BigDecimal.TEN, "mail"));
    }

    @Test
    void transfer_toCardNotFound() {
        Card from = new Card();
        from.setId(1L);
        from.setBalance(BigDecimal.TEN);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 2L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        when(cardRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardOperationsService.transfer(1L, 2L, BigDecimal.ONE, "mail"));
    }

    @Test
    void transfer_sameCard() {
        Card from = new Card();
        from.setId(1L);
        from.setBalance(BigDecimal.TEN);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        assertThrows(CardOperationException.class, () -> cardOperationsService.transfer(1L, 1L, BigDecimal.ONE, "mail"));
    }

    @Test
    void transfer_insufficientFunds() {
        Card from = new Card();
        from.setId(1L);
        from.setBalance(BigDecimal.ONE);
        Card to = new Card();
        to.setId(2L);
        to.setBalance(BigDecimal.TEN);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 2L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(to));
        assertThrows(InsufficientFundsException.class, () -> cardOperationsService.transfer(1L, 2L, new BigDecimal("5.00"), "mail"));
    }

    @Test
    void transfer_negativeAmount() {
        Card from = new Card();
        from.setId(1L);
        from.setBalance(BigDecimal.TEN);
        Card to = new Card();
        to.setId(2L);
        to.setBalance(BigDecimal.TEN);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.existsByUserEmailAndCardId("mail", 2L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(from));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(to));
        assertThrows(NegativeAmountException.class, () -> cardOperationsService.transfer(1L, 2L, new BigDecimal("-1.00"), "mail"));
    }
} 