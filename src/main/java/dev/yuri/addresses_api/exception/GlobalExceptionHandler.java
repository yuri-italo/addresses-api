package dev.yuri.addresses_api.exception;

import dev.yuri.addresses_api.dto.response.ErroDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<?> handleInvalidParam(InvalidParamException ex) {
        return ResponseEntity.status(400).body(new ErroDto(ex.getMessage(), 404));
    }
}
