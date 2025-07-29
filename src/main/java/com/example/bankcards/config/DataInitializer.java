package com.example.bankcards.config;

import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Создаем администратора, если его еще нет
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = new User();
            admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setRole(User.RoleName.ADMIN);
            userRepository.save(admin);
            System.out.println("Администратор создан: admin@example.com / admin123");
        }
        // Создаем пользователя, если его еще нет
        if (!userRepository.existsByEmail("user@example.com")){
            User user = new User();
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("User User");
            user.setRole(User.RoleName.USER);
            userRepository.save(user);
            System.out.println("Пользователь создан: user@example.com / user123");
        }
    }
}