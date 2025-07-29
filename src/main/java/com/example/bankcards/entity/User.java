package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName role;

    public User(String fullName, String email, String password, RoleName role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Getter
    public enum RoleName {
        ADMIN("Администратор"),
        USER("Пользователь");

        private final String description;

        RoleName(String description) {
            this.description = description;
        }
    }
}