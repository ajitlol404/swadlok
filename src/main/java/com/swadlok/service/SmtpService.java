package com.swadlok.service;

import com.swadlok.dto.EmailRequestDto;
import com.swadlok.dto.SmtpDto.Request;
import com.swadlok.dto.SmtpDto.Response;
import com.swadlok.dto.SmtpDto.ToggleRequest;
import com.swadlok.entity.Smtp;
import com.swadlok.utility.EmailTemplate;

import java.util.List;
import java.util.UUID;

public interface SmtpService {

    Response saveSmtp(Request request);

    List<Response> getAllSmtpConfigs();

    Response getSmtpByUuid(UUID uuid);

    Response updateSmtp(UUID uuid, Request request);

    Response toggleSmtpStatus(UUID uuid, ToggleRequest toggleRequest);

    void deleteSmtp(UUID uuid);

    Response getActiveSmtp();

    Smtp getActiveSmtpEntity();

    void sendEmail(EmailRequestDto emailRequestDTO);

    void sendEmail(String toEmail, String subject, String htmlBody);

    void sendEmail(String toEmail, EmailTemplate emailTemplate, String body);

}
