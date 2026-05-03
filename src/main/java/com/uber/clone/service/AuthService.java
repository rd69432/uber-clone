package com.uber.clone.service;

import com.uber.clone.dto.*;
import com.uber.clone.entity.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser();
}