package com.silkpay.techtask.controllers;


import com.silkpay.techtask.dto.AuthenticationDTO;
import com.silkpay.techtask.dto.RegistrationDto;
import com.silkpay.techtask.exception.ApiRequestException;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.pojo.JwtResponse;
import com.silkpay.techtask.repository.UserRepository;
import com.silkpay.techtask.security.JwtUtil;
import com.silkpay.techtask.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    final RegistrationService registrationService;
    final UserRepository userRepository;
    final AuthenticationManager authenticationManager;
    final JwtUtil jwtUtil;


    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>(new ApiRequestException("User by username : " + user.getUsername() + " already exists "), HttpStatus.BAD_REQUEST);
        }
        User savedUser = registrationService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> loggingIn(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            throw new ApiRequestException(e.getMessage());
        }
        String username = (authInputToken.getPrincipal().toString());
        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        Optional<User> user = userRepository.findByUsername(username);
        HttpHeaders newHttpHeaders = new HttpHeaders();
        newHttpHeaders.add("Jwt-Token", token);
        if (user.isEmpty()) {
            return new ResponseEntity<>(new JwtResponse("FAILED"), HttpStatus.NOT_FOUND);
        } else {
            JwtResponse response = new JwtResponse("SUCCESS",token, user.get().getId(), user.get().getUsername());
            return new ResponseEntity<>(response ,  HttpStatus.OK);
        }
    }
}
