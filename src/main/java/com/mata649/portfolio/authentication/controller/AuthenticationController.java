package com.mata649.portfolio.authentication.controller;

import com.mata649.portfolio.authentication.dtos.AuthenticatedUserResponse;
import com.mata649.portfolio.authentication.dtos.LoginUserRequest;
import com.mata649.portfolio.authentication.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserResponse> login(@Valid @RequestBody LoginUserRequest request) {

        AuthenticatedUserResponse resp = authenticationService.login(request);
        return ResponseEntity.ok(resp);
    }
}
