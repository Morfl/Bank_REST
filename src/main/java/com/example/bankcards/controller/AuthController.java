package com.example.bankcards.controller;


import com.example.bankcards.dto.LoginResponseDTO;
import com.example.bankcards.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginResponseDTO loginResponseDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginResponseDTO.getEmail(), loginResponseDTO.getPassword()
                    )
            );
            String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());

            // Устанавливаем токен в куки
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60); // 1 день
            response.addCookie(cookie);

            return ResponseEntity.ok().body("Успешная аутентификация");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка аутентификации: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Устанавливаем срок действия в 0, чтобы удалить куки
        response.addCookie(cookie);
        return ResponseEntity.ok("Вы успешно вышли из системы");
    }
}
