package com.mata649.portfolio.authentication.dtos;

import java.util.UUID;

public record AuthenticatedUserResponse(
        UUID id,
        String email,
        String jwt
) {
}
