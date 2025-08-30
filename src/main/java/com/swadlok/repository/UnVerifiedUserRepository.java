package com.swadlok.repository;

import com.swadlok.entity.UnVerifiedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface UnVerifiedUserRepository extends JpaRepository<UnVerifiedUser, UUID> {

    boolean existsByEmail(String email);

    default UnVerifiedUser findUnVerifiedUserByUuid(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("UnVerifiedUser with [UUID= " + uuid + "] not found"));
    }

    Optional<UnVerifiedUser> findByEmailIgnoreCase(String email);

    default UnVerifiedUser findUnVerifiedUserByEmail(String email) {
        return findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NoSuchElementException("UnVerifiedUser with [EMAIL= " + email + "] not found"));
    }

    @Modifying
    @Transactional
    @Query("DELETE FROM UnVerifiedUser u WHERE u.createdAt < :expiryTime")
    int deleteAllExpired(ZonedDateTime expiryTime);
}
