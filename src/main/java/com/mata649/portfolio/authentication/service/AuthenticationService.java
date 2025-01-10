package com.mata649.portfolio.authentication.service;

import com.mata649.portfolio.authentication.dtos.AuthenticatedUserResponse;
import com.mata649.portfolio.authentication.dtos.LoginUserRequest;
import com.mata649.portfolio.user.dtos.UserResponse;
import com.mata649.portfolio.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthenticationService(JwtService jwtService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    private Map<String, Object> generateExtraClaims(UserResponse response) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("currentUserID", response.getId());
        return extraClaims;
    }

    public AuthenticatedUserResponse login(LoginUserRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authenticationToken);
        UserResponse user = userService.findByEmail(request.getEmail());
        String jwt = jwtService.generateToken(user.getEmail(), generateExtraClaims(user));
        return new AuthenticatedUserResponse(user.getId(), user.getEmail(), jwt);

    }

}
