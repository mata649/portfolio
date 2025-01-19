package com.mata649.portfolio.user.service;

import com.mata649.portfolio.user.dtos.CreateUserRequest;
import com.mata649.portfolio.user.dtos.UserResponse;
import com.mata649.portfolio.user.exceptions.EmailAlreadyTakenException;
import com.mata649.portfolio.user.exceptions.UserWithEmailNotFoundException;
import com.mata649.portfolio.user.model.User;
import com.mata649.portfolio.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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

        Optional<User> optionalUser = userRepository
                .findByEmail(request.getEmail());

        if (optionalUser.isPresent()) {
            throw new EmailAlreadyTakenException(request.getEmail());
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User userSaved = userRepository.save(user);

        return UserResponse.from(userSaved);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserWithEmailNotFoundException(email));
        return UserResponse.from(user);
    }
}
