package dev.yuri.addresses_api.exception;

import dev.yuri.addresses_api.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

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

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<?> handleInvalidFiler(InvalidFilterException ex) {
        return ResponseEntity.status(400).body(new ErrorResponse(ex.getMessage(), 400));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
       return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage(), 404));
    }

    @ExceptionHandler(EntityNotSavedException.class)
    public ResponseEntity<?> handleEntityNotSaved(EntityNotSavedException ex) {
        return ResponseEntity.status(500).body(new ErrorResponse(ex.getMessage(), 500));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<?> handleEntityAlreadyExists(EntityAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(new ErrorResponse(ex.getMessage(), 409));
    }
}