package com.swadlok.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponseDTO(
        ZonedDateTime timestamp,
        int statusCode,
        String error,
        String message,
        String path,
        String method,
        String exception,
        Map<String, List<String>> fieldErrors
) {
    // Factory method: General purpose
    public static ErrorResponseDTO of(
            int statusCode,
            String error,
            String message,
            String path,
            String method,
            String exception,
            Map<String,
                    List<String>> fieldErrors
    ) {
        return new ErrorResponseDTO(
                ZonedDateTime.now(),
                statusCode,
                error,
                message,
                path,
                method,
                exception,
                fieldErrors
        );
    }

    // Factory method: No field errors
    public static ErrorResponseDTO of(int statusCode, String error, String message, String path, String method, String exception) {
        return of(statusCode, error, message, path, method, exception, null);
    }
}
