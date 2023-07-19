package com.silkpay.techtask.controllers;

import com.silkpay.techtask.dto.AuthenticationDTO;
import com.silkpay.techtask.dto.RegistrationDto;
import com.silkpay.techtask.exception.ApiRequestException;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.pojo.JwtResponse;
import com.silkpay.techtask.repository.UserRepository;
import com.silkpay.techtask.security.JwtUtil;
import com.silkpay.techtask.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private RegistrationService registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        String username = "testuser";
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername(username);
        registrationDto.setPassword("password");

        User user = new User();
        user.setUsername(username);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(registrationService.registerUser(registrationDto)).thenReturn(user);

        ResponseEntity<?> responseEntity = authController.registerUser(registrationDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userRepository, times(1)).existsByUsername(username);
        verify(registrationService, times(1)).registerUser(registrationDto);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        String username = "testuser";
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername(username);
        registrationDto.setPassword("password");

        when(userRepository.existsByUsername(username)).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.registerUser(registrationDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof ApiRequestException);
        verify(userRepository, times(1)).existsByUsername(username);
        verify(registrationService, never()).registerUser(registrationDto);
    }

    @Test
    void testLoggingIn_Success() {
        String username = "testuser";
        String password = "password";
        String token = "generatedJwtToken";

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername(username);
        authenticationDTO.setPassword(password);

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(username, password);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);

        JwtResponse expectedResponse = new JwtResponse("SUCCESS", token, user.getId(), user.getUsername());

        when(authenticationManager.authenticate(authInputToken)).thenReturn(authInputToken);
        when(jwtUtil.generateToken(username)).thenReturn(token);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        ResponseEntity<?> responseEntity = authController.loggingIn(authenticationDTO);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof JwtResponse);
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(authenticationManager, times(1)).authenticate(authInputToken);
        verify(jwtUtil, times(1)).generateToken(username);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testLoggingIn_InvalidCredentials() {
        String username = "testuser";
        String password = "invalidPassword";

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername(username);
        authenticationDTO.setPassword(password);

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(authInputToken)).thenThrow(new BadCredentialsException("Invalid credentials"));

        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> authController.loggingIn(authenticationDTO));

        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(authInputToken);
        verify(jwtUtil, never()).generateToken(anyString());
        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    void testLoggingIn_UserNotFound() {
        String username = "testuser";
        String password = "password";
        String token = "generatedJwtToken";

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername(username);
        authenticationDTO.setPassword(password);

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(username, password);

        when(authenticationManager.authenticate(authInputToken)).thenReturn(authInputToken);
        when(jwtUtil.generateToken(username)).thenReturn(token);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = authController.loggingIn(authenticationDTO);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof JwtResponse);
        JwtResponse responseBody = (JwtResponse) responseEntity.getBody();
        assertEquals("FAILED", responseBody.getStatus());
        assertNull(responseBody.getToken());
        assertNull(responseBody.getId());
        assertNull(responseBody.getUsername());
        verify(authenticationManager, times(1)).authenticate(authInputToken);
        verify(jwtUtil, times(1)).generateToken(username);
        verify(userRepository, times(1)).findByUsername(username);
    }
}