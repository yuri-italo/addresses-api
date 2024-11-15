package dev.yuri.addresses_api.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, String mensagem, LocalDateTime timestamp, String path) { }
