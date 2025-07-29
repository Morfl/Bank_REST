package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.service.CardOperationsService;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.MapperUtils;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RequestMapping("/api/v1/card")

public class CardUserController {

    private final CardService cardService;

    private final CardOperationsService cardOperationsService;


    @PutMapping("/offActivateCard/{id}")
    public ResponseEntity<CardDTO> offActivateCard(
            @Parameter(description = "ID карты", example = "1")
            @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(MapperUtils.mapCardToCardDTO(cardService.offActivateCard(id, email)));
    }

    @PutMapping("/activateCard/{id}")
    public ResponseEntity<?> activateCard(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(cardService.activateCard(id, email));
    }

    @GetMapping("/getMyCards")
    public ResponseEntity<?> getMyCards() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        try {
            return ResponseEntity.ok(cardService.getMyCards(email));
        } catch (Exception e) {
            throw new RuntimeException("Произошла непредвиденная ошибка", e);
        }
    }

    @GetMapping("/getMyCardNumber")
    public ResponseEntity<Card> getMyCardNumber(@RequestParam Long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Card card = cardService.getMyCardNumber(id, email);
        return ResponseEntity.ok(card);
    }

    @GetMapping("/getMyBalance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            BigDecimal balance = cardService.getBalance(id, email);
            if (balance == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            throw new RuntimeException("Произошла непредвиденная ошибка", e);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam Long fromCardId, @RequestParam Long toCardId, @RequestParam BigDecimal amount) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            cardOperationsService.transfer(fromCardId, toCardId, amount, email);
            return ResponseEntity.ok("Перевод успешно выполнен");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при выполнении перевода: " + e.getMessage());
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam Long cardId, @RequestParam BigDecimal amount) {
        try {
            cardOperationsService.deposit(cardId, amount);
            return ResponseEntity.ok("Пополнение успешно выполнено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при пополнении: " + e.getMessage());
        }
    }

}
