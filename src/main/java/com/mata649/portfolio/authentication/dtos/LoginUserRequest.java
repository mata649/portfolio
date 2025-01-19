package com.mata649.portfolio.authentication.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserRequest {
        @NotBlank
        private String email;
        @NotBlank
        private String password;

}
