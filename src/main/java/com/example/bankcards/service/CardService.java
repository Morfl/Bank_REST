package com.example.bankcards.service;


import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.exception.CardNotBelongUserException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;

@Service
@AllArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final UserRepository userRepository;

    public Card createBankCard(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + userId + " не найден"));
        String cardNumber;
        while (true) {
            cardNumber = CardNumberUtil.generateCardNumber();
            String encryptedCardNumber = CardNumberUtil.encryptCardNumber(cardNumber);
            if (!cardRepository.existsByCardNumber(encryptedCardNumber)) {
                Card card = new Card(user, user.getFullName(), Card.CardStatus.ACTIVE, new BigDecimal(0));
                card.setCardNumber(encryptedCardNumber);
                return cardRepository.save(card);
            }
        }
    }

    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
    }

    public Card refactorCard(Long id, String fullName, LocalDate expirationDate, Card.CardStatus status, BigDecimal balance, String cardNumber) throws Exception {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
        card.setFullName(fullName);
        card.setExpiryDate(expirationDate);
        card.setStatus(status);
        card.setBalance(balance);
        card.setCardNumber(CardNumberUtil.encryptCardNumber(cardNumber));
        return cardRepository.save(card);
    }

    public Card offActivateCard(Long id, String email) {
        if (cardRepository.existsByUserEmailAndCardId(email, id)) {
            Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
            card.setStatus(Card.CardStatus.BLOCKED);
            return cardRepository.save(card);
        }
        throw new CardNotBelongUserException("Пользователь с email " + email + " не имеет доступа к карте с ID " + id);
    }

    public Card activateCard(Long id, String email) {
        if (cardRepository.existsByUserEmailAndCardId(email, id)) {
            Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
            card.setStatus(Card.CardStatus.ACTIVE);
            return cardRepository.save(card);
        }
        throw new CardNotBelongUserException("Пользователь с email " + email + " не имеет доступа к карте с ID " + id);
    }

    public Card offActivateCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
        card.setStatus(Card.CardStatus.BLOCKED);
        return cardRepository.save(card);
    }

    public Card activateCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
        card.setStatus(Card.CardStatus.ACTIVE);
        return cardRepository.save(card);
    }

    public Card deleteCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
        cardRepository.delete(card);
        return card;
    }

    public List<Card> findCardsByUserId(Long userId) {
        return cardRepository.findByUserId(userId).orElseThrow(() -> new CardNotFoundException("Карта с ID " + userId + " не найдена"));
    }

    public List<Card> findCardsByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        return cardRepository.findAllByUserId(user.getId()).orElseThrow(() -> new CardNotFoundException("Карты пользователя с email " + email + " не найдены"));
    }

    public List<Card> getListOfBankCards() {
        List<Card> cards = cardRepository.findAll();
        if (cards.isEmpty()) {
            throw new CardNotFoundException("Список карт пуст");
        }
        return cards;
    }

    public Card getMyCardNumber(Long id, String email) throws Exception {
        if (cardRepository.existsByUserEmailAndCardId(email, id)) {
            return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
        }
        throw new CardNotBelongUserException("Пользователь с email " + email + " не имеет доступа к карте с ID " + id);
    }

    public List<Card> getMyCards(String email) throws Exception {
        return cardRepository.findByUserEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
    }

    public BigDecimal getBalance(Long id, String email) {
        if (cardRepository.existsByUserEmailAndCardId(email, id)) {
            Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException("Карта с ID " + id + " не найдена"));
            return card.getBalance();
        }
        throw new CardNotBelongUserException("Пользователь с email " + email + " не имеет доступа к карте с ID " + id);
    }

}


