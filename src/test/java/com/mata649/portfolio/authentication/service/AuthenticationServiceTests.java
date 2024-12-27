package com.mata649.portfolio.authentication.service;

import com.mata649.portfolio.authentication.dtos.AuthenticatedUserResponse;
import com.mata649.portfolio.authentication.dtos.LoginUserRequest;
import com.mata649.portfolio.user.dtos.UserResponse;
import com.mata649.portfolio.user.exceptions.UserWithEmailNotFoundException;
import com.mata649.portfolio.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void login_shouldReturnAuthenticatedUserResponse_whenCredentialsAreValid() {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "password");
        UserResponse userResponse = new UserResponse(UUID.randomUUID(), "test@example.com");

        when(authenticationManager.
                authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()))
        ).thenReturn(null);
        when(userService.findByEmail(request.email())).thenReturn(userResponse);
        when(jwtService.generateToken(
                Mockito.eq(userResponse.email()),
                Mockito.anyMap()
        )).thenReturn("jwt-token");

        AuthenticatedUserResponse response = authenticationService.login(request);

        assertEquals(userResponse.id(), response.id());
        assertEquals(userResponse.email(), response.email());
        assertEquals("jwt-token", response.jwt());
    }

    @Test
    public void login_shouldThrowAuthenticationException_whenCredentialsAreInvalid() {
        LoginUserRequest request = new LoginUserRequest("test@example.com", "wrong-password");

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(request));
    }

    @Test
    public void login_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        LoginUserRequest request = new LoginUserRequest("nonexistent@example.com", "password");

        when(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())))
                .thenReturn(null);
        when(userService.findByEmail(request.email()))
                .thenThrow(new UserWithEmailNotFoundException(request.email()));

        assertThrows(UserWithEmailNotFoundException.class, () -> authenticationService.login(request));
    }

    @Test
    public void login_shouldThrowException_whenJwtServiceFails() {

        LoginUserRequest request = new LoginUserRequest("test@example.com", "password");
        UserResponse userResponse = new UserResponse(UUID.randomUUID(), "test@example.com");

        when(authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())))
                .thenReturn(null);
        when(userService.findByEmail(request.email())).thenReturn(userResponse);
        when(jwtService.generateToken(
                Mockito.eq(userResponse.email()),
                Mockito.anyMap()
        )).thenThrow(new RuntimeException("JWT generation failed"));

        assertThrows(RuntimeException.class, () -> authenticationService.login(request));
    }
}
