package dev.yuri.addresses_api.exception;

import dev.yuri.addresses_api.dto.response.ErroDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, Object> errors = new HashMap<>();
        FieldError firstError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

        if (firstError != null) {
            errors.put("mensagem", firstError.getDefaultMessage());
            errors.put("status", HttpStatus.BAD_REQUEST.value());
        } else {
            errors.put("mensagem", "Erro de validação desconhecido.");
            errors.put("status", HttpStatus.BAD_REQUEST.value());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<?> handleInvalidParam(InvalidParamException ex) {
        return ResponseEntity.status(400).body(new ErroDto(ex.getMessage(), 404));
    }

    @ExceptionHandler(ResourceNotSavedException.class)
    public ResponseEntity<?> handleResourceNotSaved(ResourceNotSavedException ex) {
        return ResponseEntity.status(400).body(new ErroDto(ex.getMessage(), 404));
    }
}
