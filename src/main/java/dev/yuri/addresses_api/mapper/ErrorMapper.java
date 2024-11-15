package dev.yuri.addresses_api.mapper;

import dev.yuri.addresses_api.dto.response.ErrorResponse;

import java.time.LocalDateTime;

public final class ErrorMapper {

    private ErrorMapper() {
        throw new UnsupportedOperationException("ErrorResponseMapper não pode ser instanciada.");
    }

    public static ErrorResponse toResponse(int status, String error, String mensagem, String path) {
        return new ErrorResponse(status, error, mensagem, LocalDateTime.now(), path);
    }
}
