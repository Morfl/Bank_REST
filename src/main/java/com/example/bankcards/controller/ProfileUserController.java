package com.example.bankcards.controller;


import com.example.bankcards.entity.User;
import com.example.bankcards.service.ProfileService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER' and 'ADMIN')")
@RequestMapping("/api/v1/profile")
public class ProfileUserController {

    private final ProfileService profileService;

    @PostMapping("/registry")
    @PermitAll
    public ResponseEntity<?> createUser(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        if (profileService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Пользователь с таким email уже существует");
        }
        return ResponseEntity.ok(profileService.createUser(fullName, email, password));
    }

    @PutMapping("/refactorProfile")
    public ResponseEntity<User> refactorUser(@RequestParam String fullName, @RequestParam String email, @RequestParam String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailNow = authentication.getName();
        return ResponseEntity.ok(profileService.refactorUser(emailNow, fullName, email, password));
    }

    @DeleteMapping("/deleteProfile")
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (profileService.deleteUser(email)){
            return ResponseEntity.ok("Пользователь успешно удален");
        }
        return ResponseEntity.badRequest().body("Не удалось удалить пользователя");
    }

    @GetMapping("/getProfile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = profileService.findByEmail(email);
        System.out.println(user);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return null;
    }

    @PutMapping("/block")
    public ResponseEntity<User> blockUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.blockUser(email));
    }

    @PutMapping("/unblock")
    public ResponseEntity<User> unblockUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.unblockUser(email));
    }
}
