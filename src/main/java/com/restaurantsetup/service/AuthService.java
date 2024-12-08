package com.restaurantsetup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.restaurantsetup.config.JwtUtil;
import com.restaurantsetup.dto.LoginRequest;
import com.restaurantsetup.dto.LoginResponse;
import com.restaurantsetup.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(LoginRequest loginRequest) {
        // Check if user exists
        System.out.println(" function login " + loginRequest.getUsername());
        com.restaurantsetup.entity.User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    System.out.println("Username not found: " + loginRequest.getUsername());
                    return new IllegalArgumentException("Invalid email or password");
                });
        System.out.println("Password : " + loginRequest.getPassword());
        System.out.println("Role " + user.getRole());
        System.out.println("Password " + user.getPassword());
        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("Username not found01: " + loginRequest.getUsername());
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getName(), user.getRole().toString());
        System.out.println("Token " + token);
        return new LoginResponse(token, user.getName(), user.getRole().toString());
    }
}