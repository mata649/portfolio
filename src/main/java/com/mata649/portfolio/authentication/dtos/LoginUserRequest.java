package com.mata649.portfolio.authentication.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
