package com.swadlok.dto;

public record EmailRequestDto(
        String toEmail,
        String subject,
        String bodyHtml
) {
}
