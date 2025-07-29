package com.example.bankcards.controller;


import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.exception.NoCardsException;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.CardNumberUtil;
import com.example.bankcards.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/card")
@PreAuthorize("hasRole('ADMIN')")
public class CardAdministrationService {

    private final CardService cardService;

    @PostMapping("/createBankCard")
    public ResponseEntity<CardDTO> createBankCard(@RequestParam Long userId) throws Exception {
        Card card = cardService.createBankCard(userId);
        return ResponseEntity.ok(MapperUtils.mapCardToCardDTO(card));
    }

    @PutMapping("/refactorCard/{id}")
    public ResponseEntity<CardDTO> refactorCard(@PathVariable Long id, @RequestParam String fullName, @RequestParam LocalDate expirationDate, @RequestParam Card.CardStatus status, @RequestParam BigDecimal balance, @RequestParam String cardNumber) throws Exception {
        Card card = cardService.refactorCard(id, fullName, expirationDate, status, balance, cardNumber);
        return ResponseEntity.ok(MapperUtils.mapCardToCardDTO(card));
    }

    @DeleteMapping("/deleteCard/{id}")
    public ResponseEntity<CardDTO> deleteCard(@PathVariable Long id) throws Exception {
        Card card = cardService.findById(id);
        return ResponseEntity.ok(MapperUtils.mapCardToCardDTO(card));
    }

    @GetMapping("/getAllListOfCards")
    public ResponseEntity<List<CardDTO>> getAllListOfCards()  {
        List<Card> cards = cardService.getListOfBankCards();
        List<CardDTO> cardDTOs = new ArrayList<>();
        for (Card card : cards) {
            cardDTOs.add(MapperUtils.mapCardToCardDTO(card));
        }
        return ResponseEntity.ok(cardDTOs);
    }

    @GetMapping("/getListOfCardByEmail/{email}")
    public ResponseEntity<List<CardDTO>> getListOfCardByEmail(@PathVariable String email) throws Exception {
        List<Card> cards = cardService.getMyCards(email);
        if (cards.isEmpty()) {
            throw new NoCardsException("Карты не были найдены по почте: " + email);
        }
        List<CardDTO> cardDTOs = new ArrayList<>();
        for (Card card : cards) {
            cardDTOs.add(MapperUtils.mapCardToCardDTO(card));
        }
        return ResponseEntity.ok(cardDTOs);
    }

    @PutMapping("/offActivateCard/{id}")
    public ResponseEntity<CardDTO> offActivateCard(@PathVariable Long id) {
        Card card = cardService.offActivateCard(id);
        return ResponseEntity.ok(MapperUtils.mapCardToCardDTO(card));
    }

    @PutMapping("/activateCard/{id}")
    public ResponseEntity<CardDTO> activateCard(@PathVariable Long id) {
        Card card = cardService.activateCard(id);
        return ResponseEntity.ok(MapperUtils.mapCardToCardDTO(card));
    }
}
