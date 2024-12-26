package com.mata649.portfolio.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

        @Email
        @NotBlank
        @Max(64)
        String email,
        @NotBlank
        @Min(8)
        @Max(64)
        String password
) {
}
