package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String cardNumber;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @Column(nullable = false)
    private BigDecimal balance;

    public Card(User user, String fullName, CardStatus status, BigDecimal balance) {
        this.user = user;
        this.fullName = fullName;
        this.status = status;
        this.balance = balance;
    }

    @PrePersist
    private void prePersist() {
        this.creationDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(3);
    }

    @Getter
    public enum CardStatus {
        ACTIVE("Активна"),
        BLOCKED("Заблокирована");

        private final String description;

        CardStatus(String description) {
            this.description = description;
        }
    }
}


