package com.mata649.portfolio.user.dtos;

import com.mata649.portfolio.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String email;

    public static UserResponse from(User userSaved) {
        return new UserResponse(userSaved.getId(), userSaved.getEmail());
    }
}
