package com.silkpay.techtask.service;

import com.silkpay.techtask.dto.RegistrationDto;
import com.silkpay.techtask.models.User;
import com.silkpay.techtask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;

    public User registerUser(RegistrationDto registrationDto){
        User user = new User();
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUsername(registrationDto.getUsername());
        return   userRepository.save(user);
    }

}
