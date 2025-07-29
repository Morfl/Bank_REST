package com.example.bankcards.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String email;
    private String password;
}