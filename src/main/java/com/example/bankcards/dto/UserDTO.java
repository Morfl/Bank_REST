package com.example.bankcards.dto;

import com.example.bankcards.entity.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private boolean active;

    private User.RoleName role;
}
