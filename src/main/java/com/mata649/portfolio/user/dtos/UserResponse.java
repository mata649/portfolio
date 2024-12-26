package com.mata649.portfolio.user.dtos;

import com.mata649.portfolio.user.model.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {
    public static UserResponse from(User userSaved) {
        return new UserResponse(userSaved.getId(), userSaved.getEmail());
    }
}
