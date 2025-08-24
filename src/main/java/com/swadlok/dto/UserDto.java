package com.swadlok.dto;

import com.swadlok.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.swadlok.entity.User.Role.ROLE_ADMIN;
import static java.lang.Boolean.TRUE;

public class UserDto {

    public record AdminRequest(
            @NotBlank(message = "Name is required")
            @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
            String name,

            @NotBlank(message = "Email is required")
            @Email(message = "Invalid email format")
            String email,

            @NotBlank(message = "Password is required")
            @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
            String password,

            @NotBlank(message = "Phone number is required")
            @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone number must be 10 digits and start with 6, 7, 8, or 9")
            String phoneNumber,

            String image
    ) {
        public User toEntity(String encodedPassword) {
            return User.builder()
                    .name(name.strip().toLowerCase())
                    .email(email.strip().toLowerCase())
                    .password(encodedPassword)
                    .phoneNumber(phoneNumber)
                    .role(ROLE_ADMIN)
                    .isActive(TRUE)
                    .build();
        }
    }

    public record AdminResponse(
            String code,
            String name,
            String email,
            String phoneNumber
    ) {
        public static AdminResponse fromEntity(User user) {
            return new AdminResponse(
                    user.getCode(),
                    user.getName(),
                    user.getEmail(),
                    user.getPhoneNumber()
            );
        }
    }

}
