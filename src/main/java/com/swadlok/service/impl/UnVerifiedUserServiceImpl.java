package com.swadlok.service.impl;

import com.swadlok.dto.UnVerifiedUserDto.PasswordRequest;
import com.swadlok.dto.UnVerifiedUserDto.PublicResponse;
import com.swadlok.dto.UnVerifiedUserDto.Request;
import com.swadlok.entity.UnVerifiedUser;
import com.swadlok.exception.SmtpException;
import com.swadlok.exception.UnVerifiedUserException;
import com.swadlok.repository.UnVerifiedUserRepository;
import com.swadlok.service.SmtpService;
import com.swadlok.service.UnVerifiedUserService;
import com.swadlok.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.swadlok.utility.AppConstant.*;
import static com.swadlok.utility.EmailTemplate.ACCOUNT_CREATED_SUCCESS_TEMPLATE;
import static com.swadlok.utility.EmailTemplate.UNVERIFIED_USER_REGISTRATION_TEMPLATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnVerifiedUserServiceImpl implements UnVerifiedUserService {

    private final UnVerifiedUserRepository unVerifiedUserRepository;
    private final UserService userService;
    private final SmtpService smtpService;

    @Override
    @Transactional(rollbackFor = SmtpException.class)
    public PublicResponse registerUnVerifiedUser(Request unVerifiedUserRequestDTO) {
        if (unVerifiedUserRepository.existsByEmail(unVerifiedUserRequestDTO.email())) {
            throw new UnVerifiedUserException("The email you have chosen is currently pending verification. Please check your email for the verification link or choose a different email.");
        }

        if (userService.userExistsByEmail(unVerifiedUserRequestDTO.email())) {
            throw new UnVerifiedUserException("The email you have entered is already associated with a verified account. Please use a different email.");
        }

        UnVerifiedUser unVerifiedUser = unVerifiedUserRepository.save(unVerifiedUserRequestDTO.toEntity());

        // Send verification email
        String link = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/unverified-users/" + unVerifiedUser.getId() + "/verify")
                .build()
                .toUriString();

        try {

            smtpService.sendEmail(
                    unVerifiedUserRequestDTO.email(),
                    UNVERIFIED_USER_REGISTRATION_TEMPLATE,
                    UNVERIFIED_USER_REGISTRATION_TEMPLATE.formatBody(
                            StringUtils.capitalize(unVerifiedUserRequestDTO.name()),
                            BRAND_NAME,
                            link,
                            VERIFICATION_EXPIRATION_MINUTES,
                            SUPPORT_TEAM
                    )
            );

        } catch (Exception e) {
            log.error("Email sending failed for unverified user: {}", unVerifiedUserRequestDTO.email(), e);
            throw new SmtpException("Failed to send verification email. Please try again later.");
        }

        return PublicResponse.fromEntity(unVerifiedUser);
    }

    @Override
    public PublicResponse verifyLink(UUID uuid) {
        try {
            UnVerifiedUser unVerifiedUser = unVerifiedUserRepository.findUnVerifiedUserByUuid(uuid);
            if (isExpired(unVerifiedUser.getCreatedAt())) {
                unVerifiedUserRepository.delete(unVerifiedUser);
                throw new UnVerifiedUserException("Verification link has expired");
            }
            return PublicResponse.fromEntity(unVerifiedUser);
        } catch (NoSuchElementException nsee) {
            throw new UnVerifiedUserException("Verification link has expired");
        }
    }

    @Override
    public void verifyAndSaveUser(UUID uuid, PasswordRequest unVerifiedUserPasswordDTO) {
        try {

            UnVerifiedUser unVerifiedUser = unVerifiedUserRepository.findUnVerifiedUserByUuid(uuid);
            if (isExpired(unVerifiedUser.getCreatedAt())) {
                unVerifiedUserRepository.delete(unVerifiedUser);
                throw new UnVerifiedUserException("Verification link has expired");
            }

            userService.createCustomer(
                    unVerifiedUser.getName(),
                    unVerifiedUser.getEmail(),
                    unVerifiedUserPasswordDTO.password(),
                    unVerifiedUser.getPhoneNumber()
            );

            unVerifiedUserRepository.delete(unVerifiedUser);

            smtpService.sendEmail(
                    unVerifiedUser.getEmail(),
                    ACCOUNT_CREATED_SUCCESS_TEMPLATE,
                    ACCOUNT_CREATED_SUCCESS_TEMPLATE.formatBody(
                            StringUtils.capitalize(unVerifiedUser.getName()),
                            BRAND_NAME,
                            unVerifiedUser.getEmail(),
                            SUPPORT_TEAM
                    )
            );

        } catch (NoSuchElementException nsee) {
            log.warn("Verification failed: UnVerifiedUser not found for UUID={}", uuid);
            throw new UnVerifiedUserException("Verification link has expired");
        }
    }

    @Override
    @Transactional
    public void cleanUpExpiredUnverifiedUsers() {
        ZonedDateTime expiryTime = ZonedDateTime.now().minusMinutes(VERIFICATION_EXPIRATION_MINUTES);
        int deletedCount = unVerifiedUserRepository.deleteAllExpired(expiryTime);
        log.info("Expired unverified users cleanup completed. Deleted: {}", deletedCount);
    }

    private boolean isExpired(ZonedDateTime createdAt) {
        return createdAt
                .plusMinutes(VERIFICATION_EXPIRATION_MINUTES)
                .isBefore(ZonedDateTime.now());
    }
}
