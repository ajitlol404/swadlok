package com.swadlok.dto;

import com.swadlok.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

import static com.swadlok.utility.AppConstant.MIN_STRING_DEFAULT_SIZE;

public class ProfileDto {

    public record Request(
            @NotBlank(message = "Name must not be blank")
            @Size(min = MIN_STRING_DEFAULT_SIZE, max = 50, message = "Name must be between 3 and 50 characters")
            String name,

            @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
            String password
    ) {
    }

    public record Response(
            UUID id,
            String code,
            String name,
            String email,
            String role,
            boolean active
    ) {
        public static Response fromEntity(User user) {
            return new Response(
                    user.getId(),
                    user.getCode(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().name(),
                    user.isActive()
            );
        }
    }
}
