package com.example.bankcards.service;


import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(String fullName, String email, String password) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.RoleName.USER);
        return userRepository.save(user);
    }

    public User createAdmin(String fullName, String email, String password) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.RoleName.ADMIN);
        return userRepository.save(user);
    }

    public List<User> getListOfUsers() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    public User blockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        user.setActive(false);
        return userRepository.save(user);
    }

    public User blockUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        user.setActive(false);
        return userRepository.save(user);
    }

    public User unblockUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        user.setActive(true);
        return userRepository.save(user);
    }

    public User unblockUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        user.setActive(true);
        return userRepository.save(user);
    }

    public User refactorUser(Long id, String fullName, String email, String password) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User refactorUser(String emailNow, String fullName, String email, String password) {
        User user = userRepository.findByEmail(emailNow).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + emailNow + " не найден"));
        if (!emailNow.equals(email) && userRepository.existsByEmail(email)) {
            throw new UserNotFoundException("Пользователь с email " + email + " уже существует");
        }
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + id + " не найден"));
        userRepository.delete(user);
        return true;
    }

    public boolean deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь с email " + email + " не найден"));
        userRepository.delete(user);
        return true;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
