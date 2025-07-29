package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User user = profileService.createUser("Test User", "test@mail.com", "pass");
        assertNotNull(user);
        assertEquals("Test User", user.getFullName());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("encoded", user.getPassword());
    }

    @Test
    void findByEmail_userExists_success() {
        User user = new User();
        user.setEmail("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        User found = profileService.findByEmail("test@mail.com");
        assertEquals(user, found);
    }

    @Test
    void findByEmail_userNotFound_throwsException() {
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.findByEmail("test@mail.com"));
    }

    @Test
    void createAdmin_success() {
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User user = profileService.createAdmin("Admin", "admin@mail.com", "pass");
        assertNotNull(user);
        assertEquals("Admin", user.getFullName());
        assertEquals("admin@mail.com", user.getEmail());
        assertEquals("encoded", user.getPassword());
        assertEquals(User.RoleName.ADMIN, user.getRole());
    }

    @Test
    void getListOfUsers_success() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(users, profileService.getListOfUsers());
    }

    @Test
    void findById_success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertEquals(user, profileService.findById(1L));
    }

    @Test
    void findById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.findById(1L));
    }

    @Test
    void blockUser_byId_success() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User result = profileService.blockUser(1L);
        assertFalse(result.isActive());
    }

    @Test
    void blockUser_byId_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.blockUser(1L));
    }

    @Test
    void blockUser_byEmail_success() {
        User user = new User();
        user.setEmail("mail");
        user.setActive(true);
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User result = profileService.blockUser("mail");
        assertFalse(result.isActive());
    }

    @Test
    void blockUser_byEmail_notFound() {
        when(userRepository.findByEmail("mail")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.blockUser("mail"));
    }

    @Test
    void unblockUser_byId_success() {
        User user = new User();
        user.setId(1L);
        user.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User result = profileService.unblockUser(1L);
        assertTrue(result.isActive());
    }

    @Test
    void unblockUser_byId_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.unblockUser(1L));
    }

    @Test
    void unblockUser_byEmail_success() {
        User user = new User();
        user.setEmail("mail");
        user.setActive(false);
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User result = profileService.unblockUser("mail");
        assertTrue(result.isActive());
    }

    @Test
    void unblockUser_byEmail_notFound() {
        when(userRepository.findByEmail("mail")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.unblockUser("mail"));
    }

    @Test
    void refactorUser_byId_success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        User result = profileService.refactorUser(1L, "New Name", "new@mail.com", "pass");
        assertEquals("New Name", result.getFullName());
        assertEquals("new@mail.com", result.getEmail());
        assertEquals("encoded", result.getPassword());
    }

    @Test
    void refactorUser_byId_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.refactorUser(1L, "n", "e", "p"));
    }

    @Test
    void refactorUser_byEmail_success() {
        User user = new User();
        user.setEmail("mail");
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        User result = profileService.refactorUser("mail", "New Name", "new@mail.com", "pass");
        assertEquals("New Name", result.getFullName());
        assertEquals("new@mail.com", result.getEmail());
        assertEquals("encoded", result.getPassword());
    }

    @Test
    void refactorUser_byEmail_notFound() {
        when(userRepository.findByEmail("mail")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.refactorUser("mail", "n", "e", "p"));
    }

    @Test
    void refactorUser_byEmail_alreadyExists() {
        User user = new User();
        user.setEmail("mail");
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> profileService.refactorUser("mail", "n", "new@mail.com", "p"));
    }

    @Test
    void deleteUser_byId_success() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        boolean result = profileService.deleteUser(1L);
        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_byId_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.deleteUser(1L));
    }

    @Test
    void deleteUser_byEmail_success() {
        User user = new User();
        user.setEmail("mail");
        when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
        boolean result = profileService.deleteUser("mail");
        assertTrue(result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_byEmail_notFound() {
        when(userRepository.findByEmail("mail")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> profileService.deleteUser("mail"));
    }

    @Test
    void existsByEmail_true() {
        when(userRepository.existsByEmail("mail")).thenReturn(true);
        assertTrue(profileService.existsByEmail("mail"));
    }

    @Test
    void existsByEmail_false() {
        when(userRepository.existsByEmail("mail")).thenReturn(false);
        assertFalse(profileService.existsByEmail("mail"));
    }
} 