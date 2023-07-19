package com.silkpay.techtask.service;

import com.silkpay.techtask.dto.RegistrationDto;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        String username = "testuser";
        String password = "password";
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername(username);
        registrationDto.setPassword(password);

        User user = new User();
        user.setUsername(username);

        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = registrationService.registerUser(registrationDto);

        assertNotNull(savedUser);
        assertEquals(username, savedUser.getUsername());
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
