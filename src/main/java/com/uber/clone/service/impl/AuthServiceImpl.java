package com.uber.clone.service.impl;

import com.uber.clone.dto.*;
import com.uber.clone.entity.User;
import com.uber.clone.enums.Role;
import com.uber.clone.exception.BadRequestException;
import com.uber.clone.exception.ResourceNotFoundException;
import com.uber.clone.repository.UserRepository;
import com.uber.clone.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone already exists");
        }

        User user = User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(request.getPassword()) // In production, encode this!
            .phone(request.getPhone())
            .role(request.getRole())
            .active(true)
            .emailVerified(false)
            .build();

        user = userRepository.save(user);

        return AuthResponse.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .role(user.getRole().name())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // In production, verify password here!
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        return AuthResponse.builder()
            .userId(user.getId())
            .email(user.getEmail())
            .role(user.getRole().name())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .build();
    }

    @Override
    public UserResponse getCurrentUser() {
        // This would be implemented with security context
        return null;
    }
}