package com.swadlok.repository;

import com.swadlok.entity.User;
import com.swadlok.entity.User.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByRole(Role role);

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);

    default User findUserByEmail(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with [EMAIL: " + email + "] not found"));
    }

    List<User> findAllByRole(Role role);

    Optional<User> findByCode(String code);

    Page<User> findByRole(Role role, Pageable pageable);

    Page<User> findByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCaseOrRoleAndCodeContainingIgnoreCase(
            Role role1, String name,
            Role role2, String email,
            Role role3, String code,
            Pageable pageable
    );

    long countByRole(Role role);

    default User findUserById(UUID id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with [UUID: " + id + "] not found"));
    }

    default User findUserByCode(String code) {
        return findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("User with [CODE: " + code + "] not found"));
    }

    boolean existsByPhoneNumber(String phoneNumber);

}