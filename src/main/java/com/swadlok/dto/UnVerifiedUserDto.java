package com.swadlok.dto;

import com.swadlok.entity.UnVerifiedUser;
import com.swadlok.utility.AppUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.swadlok.utility.AppConstant.MIN_STRING_DEFAULT_SIZE;
import static com.swadlok.utility.AppConstant.PASSWORD_REGEX;

public class UnVerifiedUserDto {

    public record Request(
            @NotBlank(message = "{unverified.user.name.required}")
            @Size(min = MIN_STRING_DEFAULT_SIZE, max = 50, message = "{unverified.user.name.size}")
            @Pattern(regexp = "^[A-Za-z0-9\\-'/()& ]+$", message = "{unverified.user.name.pattern}")
            String name,

            @NotBlank(message = "{unverified.user.email.required}")
            @Email(message = "{unverified.user.email.invalid}")
            @Size(max = 320, message = "{unverified.user.email.size}")
            String email,

            @NotBlank(message = "{unverified.user.phoneNumber.required}")
            @Size(min = 10, max = 10, message = "{unverified.user.phoneNumber.size}")
            @Pattern(regexp = "^[6-9][0-9]{9}$", message = "{unverified.user.phoneNumber.pattern}")
            String phoneNumber
    ) {

        public UnVerifiedUser toEntity() {
            return UnVerifiedUser.builder()
                    .name(name)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .build();
        }

        public UnVerifiedUser updateUnVerifiedUser(UnVerifiedUser unVerifiedUser) {
            unVerifiedUser.setName(name);
            unVerifiedUser.setEmail(email);
            unVerifiedUser.setPhoneNumber(phoneNumber);
            return unVerifiedUser;
        }
    }

    public record Response(UUID uuid, String name, String email) {

        public static Response fromEntity(UnVerifiedUser unVerifiedUser) {
            return new Response(
                    unVerifiedUser.getId(),
                    unVerifiedUser.getName(),
                    AppUtil.maskedEmail(unVerifiedUser.getEmail()));
        }
    }

    public record PublicResponse(UUID uuid, String name, String email, long expiresInSeconds) {

        public static PublicResponse fromEntity(UnVerifiedUser unVerifiedUser) {

            ZonedDateTime now = ZonedDateTime.now();
            long expiresInSeconds = Duration.between(now, unVerifiedUser.getCreatedAt().plusMinutes(15)).getSeconds();

            // Prevent negative values
            expiresInSeconds = Math.max(expiresInSeconds, 0);

            return new PublicResponse(
                    unVerifiedUser.getId(),
                    unVerifiedUser.getName(),
                    AppUtil.maskedEmail(unVerifiedUser.getEmail()),
                    expiresInSeconds
            );
        }
    }

    public record PasswordRequest(
            @NotBlank(message = "{unverified.user.password.required}")
            @Size(min = 8, max = 30, message = "{unverified.user.password.size}")
            @Pattern(regexp = PASSWORD_REGEX, message = "{unverified.user.password.pattern}")
            String password) {
    }

}
