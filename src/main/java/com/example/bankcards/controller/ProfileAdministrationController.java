package com.example.bankcards.controller;


import com.example.bankcards.entity.User;
import com.example.bankcards.service.ProfileService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/profile")
@PreAuthorize("hasRole('ADMIN')")
public class ProfileAdministrationController {

    private final ProfileService profileService;

    @PostMapping("/registryAdmin")
    @PermitAll
    public ResponseEntity<?> createAdmin(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        if (profileService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Пользователь с таким email уже существует");
        }
        return ResponseEntity.ok(profileService.createAdmin(fullName, email, password));
    }

    @PutMapping("/unblockUser/{email}")
    public ResponseEntity<User> unblockUser(@PathVariable String  email) {
        return ResponseEntity.ok(profileService.unblockUser(email));
    }

    @PutMapping("/blockUser/{email}")
    public ResponseEntity<User> blockUser(@PathVariable String email) {
        return ResponseEntity.ok(profileService.blockUser(email));
    }

    @PutMapping("/blockUser/{id}")
    public ResponseEntity<User> blockUser(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.blockUser(id));
    }

    @PutMapping("/unblockUser/{id}")
    public ResponseEntity<User> unblockUser(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.unblockUser(id));
    }

    @PutMapping("/GetUser/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(profileService.findByEmail(email));
    }

    @PutMapping("/GetUser/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.findById(id));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getListOfUsers() {
        return ResponseEntity.ok(profileService.getListOfUsers());
    }

    @PutMapping("/refactorUser/{id}")
    public ResponseEntity<User> refactorUser(@PathVariable Long id, @RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(profileService.refactorUser(id, fullName, email, password));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (profileService.deleteUser(id)){
            return ResponseEntity.ok("Пользователь успешно удален");
        }
        return ResponseEntity.badRequest().body("Пользователь не найден");
    }

    @PutMapping("/refactorUser/{email}")
    public ResponseEntity<User> refactorUser(@PathVariable String emailNow, @RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(profileService.refactorUser(emailNow, fullName, email, password));
    }

    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email) {
        profileService.deleteUser(email);
        return ResponseEntity.ok("Пользователь успешно удален");
    }

}
