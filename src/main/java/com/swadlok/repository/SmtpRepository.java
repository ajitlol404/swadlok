package com.swadlok.repository;

import com.swadlok.entity.Smtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public interface SmtpRepository extends JpaRepository<Smtp, UUID> {

    boolean existsByName(String name);

    default Smtp findSmtpByUuid(UUID uuid) {
        return findById(uuid)
                .orElseThrow(() -> new NoSuchElementException("Smtp with [UUID: " + uuid + "] not found"));
    }

    @Modifying
    @Query("UPDATE Smtp s SET s.isActive = false WHERE s.isActive = true")
    void deactivateAll();

    long countByIsActiveTrue();

    Optional<Smtp> findByIsActiveTrue();

    default Smtp findActiveSmtp() {
        return findByIsActiveTrue()
                .orElseThrow(() -> new NoSuchElementException("No active SMTP configuration found"));
    }

}