package com.swadlok.service.impl;

import com.swadlok.dto.EmailRequestDto;
import com.swadlok.dto.SmtpDto.Request;
import com.swadlok.dto.SmtpDto.Response;
import com.swadlok.dto.SmtpDto.ToggleRequest;
import com.swadlok.entity.Smtp;
import com.swadlok.exception.ApplicationException;
import com.swadlok.repository.SmtpRepository;
import com.swadlok.service.SmtpService;
import com.swadlok.utility.AppUtil;
import com.swadlok.utility.EmailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import static com.swadlok.utility.AppConstant.*;
import static java.lang.Boolean.TRUE;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpServiceImpl implements SmtpService {

    private final SmtpRepository smtpRepository;

    @Override
    @Transactional
    public Response saveSmtp(Request smtpRequestDTO) {
        // Check if the max limit of 5 configurations has been reached
        if (smtpRepository.count() >= MAX_SMTP_CONFIGURATIONS) {
            log.warn("SMTP config limit reached [MAX_ALLOWED: {}]", MAX_SMTP_CONFIGURATIONS);
            throw new IllegalStateException("Maximum of " + MAX_SMTP_CONFIGURATIONS + " SMTP configurations allowed");
        }

        // Check if the name already exists
        if (smtpRepository.existsByName(smtpRequestDTO.name())) {
            log.warn("SMTP config already exists [SMTP_NAME: {}]", smtpRequestDTO.name());
            throw new IllegalArgumentException("SMTP configuration with the same name already exists");
        }

        testSmtpConfiguration(smtpRequestDTO);

        // If this config is marked as active, deactivate all existing active configs
        if (TRUE.equals(smtpRequestDTO.isActive())) {
            log.info("New SMTP is marked active. Deactivating others.");
            smtpRepository.deactivateAll(); // custom update query we'll define below
        }

        return Response.fromEntity(smtpRepository.save(smtpRequestDTO.toEntity()));
    }

    @Override
    public List<Response> getAllSmtpConfigs() {
        return smtpRepository.findAll()
                .stream()
                .map(Response::fromEntity)
                .toList();
    }

    @Override
    public Response getSmtpByUuid(UUID uuid) {
        return Response.fromEntity(smtpRepository.findSmtpByUuid(uuid));
    }

    @Override
    @Transactional
    public Response updateSmtp(UUID uuid, Request smtpRequestDTO) {
        log.info("Updating SMTP [UUID: {}] [NEW_NAME: {}]", uuid, smtpRequestDTO.name());

        // Find existing entity
        Smtp existing = smtpRepository.findSmtpByUuid(uuid);

        // Prevent disabling the only active config
        if (existing.isActive() && !TRUE.equals(smtpRequestDTO.isActive())
                && smtpRepository.countByIsActiveTrue() == 1) {
            log.warn("Attempted to deactivate the only active SMTP [UUID: {}]", uuid);
            throw new ApplicationException("Cannot disable the only active SMTP configuration");
        }

        // Ensure the configuration works before saving
        testSmtpConfiguration(smtpRequestDTO);

        // Deactivate others if this is set to active
        if (TRUE.equals(smtpRequestDTO.isActive())) {
            log.info("Updated SMTP marked as active, deactivating others");
            smtpRepository.deactivateAll();
        }

        // Update entity
        return Response.fromEntity(smtpRepository.save(smtpRequestDTO.updateSmtp(existing)));
    }

    @Override
    @Transactional
    public Response toggleSmtpStatus(UUID uuid, ToggleRequest toggleRequestDTO) {
        Smtp smtp = smtpRepository.findSmtpByUuid(uuid);
        boolean isActive = toggleRequestDTO.isActive();

        log.info("Toggling SMTP status [UUID: {}] [SMTP_NAME: {}] [TO_STATUS: {}]", uuid, smtp.getName(), isActive ? "ACTIVE" : "INACTIVE");

        // Prevent disabling the only active SMTP
        if (!isActive && smtp.isActive() && smtpRepository.countByIsActiveTrue() == 1) {
            log.warn("Prevented disabling the only active SMTP [UUID: {}]", uuid);
            throw new IllegalStateException("Cannot disable the only active SMTP configuration");
        }

        if (isActive && !smtp.isActive()) {
            log.info("SMTP status toggled [UUID: {}] [SMTP_NAME: {}] [NEW_STATUS: {}]", uuid, smtp.getName(), isActive ? "ACTIVE" : "INACTIVE");
            smtpRepository.deactivateAll();
        }

        smtp.setActive(isActive);
        Smtp updated = smtpRepository.save(smtp);

        return Response.fromEntity(updated);
    }

    @Override
    @Transactional
    public void deleteSmtp(UUID uuid) {
        Smtp smtp = smtpRepository.findSmtpByUuid(uuid);

        log.info("Deleting SMTP configuration [UUID: {}] [SMTP_NAME: {}]", uuid, smtp.getName());

        // Prevent deleting if it's the only active config
        if (smtp.isActive() && smtpRepository.countByIsActiveTrue() == 1) {
            log.warn("Attempted to delete the only active SMTP [UUID: {}]", uuid);
            throw new IllegalStateException("Cannot delete the active SMTP configuration");
        }

        smtpRepository.delete(smtp);
        log.info("SMTP configuration deleted [UUID: {}] [SMTP_NAME: {}]", uuid, smtp.getName());
    }

    @Override
    public Response getActiveSmtp() {
        return Response.fromEntity(smtpRepository.findActiveSmtp());
    }

    @Override
    public Smtp getActiveSmtpEntity() {
        Smtp activeSmtp = smtpRepository.findActiveSmtp();
        return Smtp.builder()
                .name(activeSmtp.getName())
                .host(activeSmtp.getHost())
                .port(activeSmtp.getPort())
                .username(activeSmtp.getUsername())
                .password(AppUtil.decodeBase64(activeSmtp.getPassword()))
                .isSsl(activeSmtp.isSsl())
                .isActive(activeSmtp.isActive())
                .build();
    }

    @Override
    public void sendEmail(EmailRequestDto emailRequestDTO) {
        Smtp smtp = getActiveSmtpEntity();
        JavaMailSenderImpl mailSender = configureMailSender(smtp);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, TRUE, UTF_8.name());

            helper.setFrom(smtp.getUsername());
            helper.setTo(emailRequestDTO.toEmail());
            helper.setSubject(emailRequestDTO.subject());
            helper.setText(emailRequestDTO.bodyHtml(), true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ApplicationException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmail(String toEmail, String subject, String bodyHtml) {
        Smtp smtp = getActiveSmtpEntity();
        JavaMailSenderImpl mailSender = configureMailSender(smtp);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, TRUE, UTF_8.name());

            helper.setFrom(smtp.getUsername());
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(bodyHtml, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ApplicationException("Failed to send email", e);
        }
    }

    @Override
    public void sendEmail(String toEmail, EmailTemplate emailTemplate, String body) {
        Smtp smtp = getActiveSmtpEntity();
        JavaMailSenderImpl mailSender = configureMailSender(smtp);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, TRUE, UTF_8.name());

            helper.setFrom(smtp.getUsername());
            helper.setTo(toEmail);
            helper.setSubject(emailTemplate.getSubject());
            helper.setText(body, TRUE);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new ApplicationException("Failed to send email", e);
        }
    }

    private void testSmtpConfiguration(Request smtpRequestDTO) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpRequestDTO.host());
        mailSender.setPort(smtpRequestDTO.port());
        mailSender.setUsername(smtpRequestDTO.username());
        mailSender.setPassword(smtpRequestDTO.password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, TRUE.toString());
        properties.put(MAIL_SMTP_STARTTLS, Boolean.toString(smtpRequestDTO.isSsl()));

        properties.put(MAIL_SMTP_CONNECTIONTIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_TIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_WRITETIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));

        try {
            mailSender.testConnection();
        } catch (MessagingException e) {
            throw new ApplicationException("SMTP connection failed: " + e.getMessage(), e);
        }
    }

    private JavaMailSenderImpl configureMailSender(Smtp smtp) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtp.getHost());
        mailSender.setPort(smtp.getPort());
        mailSender.setUsername(smtp.getUsername());
        mailSender.setPassword(smtp.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, TRUE.toString());
        properties.put(MAIL_SMTP_STARTTLS, Boolean.toString(smtp.isSsl()));
        properties.put(MAIL_SMTP_CONNECTIONTIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_TIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));
        properties.put(MAIL_SMTP_WRITETIMEOUT, String.valueOf(SMTP_TIMEOUT_MS));

        return mailSender;
    }

}
