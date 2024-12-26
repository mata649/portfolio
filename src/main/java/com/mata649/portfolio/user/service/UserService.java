package com.mata649.portfolio.user.service;

import com.mata649.portfolio.user.dtos.CreateUserRequest;
import com.mata649.portfolio.user.dtos.UserResponse;
import com.mata649.portfolio.user.exceptions.EmailAlreadyTakenException;
import com.mata649.portfolio.user.exceptions.UserWithEmailNotFoundException;
import com.mata649.portfolio.user.model.User;
import com.mata649.portfolio.user.repository.UserRepository;
import jakarta.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(CreateUserRequest request) {

        userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new EmailAlreadyTakenException(request.email()));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        User userSaved = userRepository.save(user);

        return UserResponse.from(userSaved);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserWithEmailNotFoundException(email));
        return UserResponse.from(user);
    }
}
