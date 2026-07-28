package com.vbank.user_service.user;

import com.vbank.user_service.user.dto.*;
import com.vbank.user_service.user.exception.ConflictException;
import com.vbank.user_service.user.exception.NotFoundException;
import com.vbank.user_service.user.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername()))
            throw new ConflictException("Username or Email already exists");
        if(userRepository.existsByEmail(registerRequest.getEmail()))
            throw new ConflictException("Username or Email already exists");


        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                "User Registered Successfully"
        );
}

    public LoginResponse login( LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid username or password"));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash()))
            throw new UnauthorizedException("Invalid Username or Password");

        return new LoginResponse(
                user.getId(),
                user.getUsername()
            );
    }

    public UserProfileResponse getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User with id " + userId + " not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public boolean isUserExists(UUID userId) {
        return userRepository.existsById(userId);
    }
}
