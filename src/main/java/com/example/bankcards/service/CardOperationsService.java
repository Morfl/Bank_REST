package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CardOperationsService {

    private final CardRepository cardRepository;

    public void deposit(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Карта с ID " + cardId + " не найдена"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException("Сумма пополнения должна быть положительной");
        }
        card.setBalance(card.getBalance().add(amount));
        cardRepository.save(card);
    }

    public void transfer(Long fromCardId, Long toCardId, BigDecimal amount, String email) {
        if (!cardRepository.existsByUserEmailAndCardId(email, fromCardId)) {
            throw new CardNotBelongUserException("Карта отправителя не принадлежит пользователю с указанной почтой");
        }
        if (!cardRepository.existsByUserEmailAndCardId(email, toCardId)) {
            throw new CardNotBelongUserException("Карта получателя не принадлежит пользователю с указанной почтой");
        }

        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new CardNotFoundException("Карта отправителя с ID " + fromCardId + " не найдена"));
        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new CardNotFoundException("Карта получателя с ID " + toCardId + " не найдена"));

        if (fromCardId.equals(toCardId)) {
            throw new CardOperationException("Нельзя перевести деньги на ту же карту");
        }

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на карте отправителя");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeAmountException("Сумма перевода должна быть положительной");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);
    }

}
