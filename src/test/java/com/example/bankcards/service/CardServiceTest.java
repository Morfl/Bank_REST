package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotBelongUserException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBankCard_userExists_success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFullName("Test User");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.existsByCardNumber(anyString())).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card card = cardService.createBankCard(1L);
        assertNotNull(card);
        assertEquals(user, card.getUser());
    }

    @Test
    void createBankCard_userNotFound_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> cardService.createBankCard(1L));
    }

    @Test
    void findById_cardExists_success() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Card found = cardService.findById(1L);
        assertEquals(card, found);
    }

    @Test
    void findById_cardNotFound_throwsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.findById(1L));
    }

    @Test
    void refactorCard_success() throws Exception {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card result = cardService.refactorCard(1L, "New Name", LocalDate.now(), Card.CardStatus.BLOCKED, BigDecimal.TEN, "1234");
        assertEquals("New Name", result.getFullName());
        assertEquals(Card.CardStatus.BLOCKED, result.getStatus());
        assertEquals(BigDecimal.TEN, result.getBalance());
    }

    @Test
    void refactorCard_cardNotFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.refactorCard(1L, "n", LocalDate.now(), Card.CardStatus.ACTIVE, BigDecimal.ZERO, "1"));
    }

    @Test
    void offActivateCard_withEmail_success() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card result = cardService.offActivateCard(1L, "mail");
        assertEquals(Card.CardStatus.BLOCKED, result.getStatus());
    }

    @Test
    void offActivateCard_withEmail_notBelong() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(false);
        assertThrows(CardNotBelongUserException.class, () -> cardService.offActivateCard(1L, "mail"));
    }

    @Test
    void activateCard_withEmail_success() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card result = cardService.activateCard(1L, "mail");
        assertEquals(Card.CardStatus.ACTIVE, result.getStatus());
    }

    @Test
    void activateCard_withEmail_notBelong() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(false);
        assertThrows(CardNotBelongUserException.class, () -> cardService.activateCard(1L, "mail"));
    }

    @Test
    void offActivateCard_byId_success() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card result = cardService.offActivateCard(1L);
        assertEquals(Card.CardStatus.BLOCKED, result.getStatus());
    }

    @Test
    void activateCard_byId_success() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenAnswer(i -> i.getArgument(0));
        Card result = cardService.activateCard(1L);
        assertEquals(Card.CardStatus.ACTIVE, result.getStatus());
    }

    @Test
    void deleteCard_success() {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Card result = cardService.deleteCard(1L);
        assertEquals(card, result);
        verify(cardRepository).delete(card);
    }

    @Test
    void deleteCard_notFound() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.deleteCard(1L));
    }

    @Test
    void findCardsByUserId_success() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        when(cardRepository.findByUserId(1L)).thenReturn(Optional.of(cards));
        List<Card> result = cardService.findCardsByUserId(1L);
        assertEquals(cards, result);
    }

    @Test
    void findCardsByUserId_notFound() {
        when(cardRepository.findByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.findCardsByUserId(1L));
    }

    @Test
    void findCardsByEmail_success() {
        User user = new User();
        user.setId(1L);
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(cardRepository.findAllByUserId(1L)).thenReturn(Optional.of(cards));
        List<Card> result = cardService.findCardsByEmail("mail");
        assertEquals(cards, result);
    }

    @Test
    void findCardsByEmail_userNotFound() {
        when(userRepository.findByEmail("mail")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> cardService.findCardsByEmail("mail"));
    }

    @Test
    void findCardsByEmail_cardsNotFound() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(cardRepository.findAllByUserId(1L)).thenReturn(Optional.empty());
        assertThrows(CardNotFoundException.class, () -> cardService.findCardsByEmail("mail"));
    }

    @Test
    void getListOfBankCards_success() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        when(cardRepository.findAll()).thenReturn(cards);
        List<Card> result = cardService.getListOfBankCards();
        assertEquals(cards, result);
    }

    @Test
    void getListOfBankCards_empty() {
        when(cardRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(CardNotFoundException.class, () -> cardService.getListOfBankCards());
    }

    @Test
    void getMyCardNumber_success() throws Exception {
        Card card = new Card();
        card.setId(1L);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        Card result = cardService.getMyCardNumber(1L, "mail");
        assertEquals(card, result);
    }

    @Test
    void getMyCardNumber_notBelong() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(false);
        assertThrows(CardNotBelongUserException.class, () -> cardService.getMyCardNumber(1L, "mail"));
    }

    @Test
    void getMyCards_success() throws Exception {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card());
        when(cardRepository.findByUserEmail("mail")).thenReturn(Optional.of(cards));
        List<Card> result = cardService.getMyCards("mail");
        assertEquals(cards, result);
    }

    @Test
    void getMyCards_userNotFound() {
        when(cardRepository.findByUserEmail("mail")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> cardService.getMyCards("mail"));
    }

    @Test
    void getBalance_success() {
        Card card = new Card();
        card.setId(1L);
        card.setBalance(BigDecimal.TEN);
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(true);
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        BigDecimal result = cardService.getBalance(1L, "mail");
        assertEquals(BigDecimal.TEN, result);
    }

    @Test
    void getBalance_notBelong() {
        when(cardRepository.existsByUserEmailAndCardId("mail", 1L)).thenReturn(false);
        assertThrows(CardNotBelongUserException.class, () -> cardService.getBalance(1L, "mail"));
    }
} 