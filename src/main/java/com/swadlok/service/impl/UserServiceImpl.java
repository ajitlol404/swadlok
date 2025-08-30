package com.swadlok.service.impl;

import com.swadlok.dto.UserDto.AdminRequest;
import com.swadlok.dto.UserDto.AdminResponse;
import com.swadlok.entity.Customer;
import com.swadlok.entity.User;
import com.swadlok.exception.ApplicationException;
import com.swadlok.repository.CustomerRepository;
import com.swadlok.repository.UserRepository;
import com.swadlok.service.FileService;
import com.swadlok.service.UserService;
import com.swadlok.utility.ImageCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

import static com.swadlok.entity.User.Role.ROLE_ADMIN;
import static com.swadlok.entity.User.Role.ROLE_CUSTOMER;
import static java.lang.Boolean.TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FileService fileService;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean areThereAdminUsers() {
        return userRepository.existsByRole(ROLE_ADMIN);
    }

    @Override
    @Transactional
    public AdminResponse createAdminUser(AdminRequest adminRequest) {

        if (areThereAdminUsers()) {
            throw new ApplicationException("Admin user already exists");
        }

        try {
            return AdminResponse.fromEntity(userRepository.save(adminRequest.toEntity(passwordEncoder.encode(adminRequest.password()))));
        } catch (Exception e) {
            // Rollback image if it was uploaded
            if (adminRequest.image() != null && !adminRequest.image().isBlank()) {
                try {
                    fileService.deleteFile(ImageCategory.USER, adminRequest.image());
                    log.info("Rolled back uploaded image after user creation failure: {}", adminRequest.image());
                } catch (Exception deleteEx) {
                    log.error("Failed to delete uploaded image during rollback: {}", deleteEx.getMessage(), deleteEx);
                }
            }

            // Re-throw the original exception
            throw new ApplicationException("User creation failed. Rolled back uploaded image. Reason: " + e.getMessage(), e);

        }
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public void createCustomer(String name, String email, String password, String phoneNumber) {

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(ROLE_CUSTOMER)
                .isActive(TRUE)
                .phoneNumber(phoneNumber)
                .build();

        userRepository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .userData(Customer.UserData.builder()
                        .secretKey(UUID.randomUUID())
                        .secretKeyStatus(false)
                        .build())
                .addresses(new ArrayList<>())
                .build();

        customerRepository.save(customer);
    }
}
