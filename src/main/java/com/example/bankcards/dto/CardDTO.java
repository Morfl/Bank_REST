package com.example.bankcards.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Данные банковской карты")
public class CardDTO {
    @Schema(description = "ID карты", example = "1")
    private Long id;
    @Schema(description = "Номер карты (маскированный)", example = "1234-****-****-5678")
    private String cardNumber;
    @Schema(description = "Баланс карты", example = "1000.00")
    private BigDecimal balance;
    @Schema(description = "Статус карты", example = "Активна")
    private String status;
    @Schema(description = "Email владельца карты", example = "user@mail.com")
    private String ownerEmail;
    @Schema(description = "Дата окончания действия карты", example = "2026-12-31")
    private LocalDate expiryDate;
} 