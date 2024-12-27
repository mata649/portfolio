package com.mata649.portfolio.user.service;

import com.mata649.portfolio.user.dtos.CreateUserRequest;
import com.mata649.portfolio.user.dtos.UserResponse;
import com.mata649.portfolio.user.exceptions.EmailAlreadyTakenException;
import com.mata649.portfolio.user.exceptions.UserWithEmailNotFoundException;
import com.mata649.portfolio.user.model.User;
import com.mata649.portfolio.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void create_shouldThrowEmailAlreadyTakenException_whenEmailIsAlreadyTaken() {
        CreateUserRequest request = new CreateUserRequest("john@doe.com", "12345678");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        EmailAlreadyTakenException expectedException = assertThrows(EmailAlreadyTakenException.class, () -> userService.create(request));

        assertEquals(String.format("The email %s has been already taken", request.email()), expectedException.getMessage());
    }

    @Test
    public void create_shouldReturnUserResponse_whenRequestIsValid() {
        CreateUserRequest request = new CreateUserRequest("john@doe.com", "12345678");
        User userSaved = new User(UUID.randomUUID(), request.email(), request.password());
        UserResponse expectedResponse = UserResponse.from(userSaved);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(userSaved);
        UserResponse response = userService.create(request);

        assertEquals(response, expectedResponse);

    }

    @Test
    public void findByEmail_shouldThrowUserWithEmailNotFoundException_whenUserNotFound() {
        String email = "john@doe.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserWithEmailNotFoundException expectedException = assertThrows(UserWithEmailNotFoundException.class, () -> userService.findByEmail(email));

        assertEquals(String.format("The user with email %s was not found", email), expectedException.getMessage());
    }

    @Test
    public void findByEmail_shouldReturnUserResponse_whenUserIsFound() {
        String email = "john@doe.com";
        User userFound = new User(UUID.randomUUID(), email, "12345678");
        UserResponse expectedUserResponse = UserResponse.from(userFound);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userFound));
        UserResponse resp = userService.findByEmail(email);

        assertEquals(resp, expectedUserResponse);
    }
}
