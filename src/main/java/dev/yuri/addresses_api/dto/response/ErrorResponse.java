package dev.yuri.addresses_api.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, String mensagem, LocalDateTime timestamp, String path) {
    public ErrorResponse(int status, String error, String mensagem, String path) {
        this(status, error, mensagem, LocalDateTime.now(), path);
    }
}
