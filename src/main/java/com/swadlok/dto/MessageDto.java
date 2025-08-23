package com.swadlok.dto;

public record MessageDto(MessageType type, String message) {
    public enum MessageType {
        DANGER, SUCCESS, WARNING, INFO
    }
}
