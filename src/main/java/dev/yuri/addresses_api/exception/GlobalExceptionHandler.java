package dev.yuri.addresses_api.exception;

import dev.yuri.addresses_api.mapper.ErrorMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> errors = new LinkedHashMap<>();
        FieldError firstError = ex.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);

        if (firstError != null) {
            errors.put("status", HttpStatus.BAD_REQUEST.value());
            errors.put("error", HttpStatus.BAD_REQUEST.toString());
            errors.put("mensagem", firstError.getDefaultMessage());
            errors.put("timestamp", LocalDateTime.now());
            errors.put("path", request.getRequestURI());
        } else {
            errors.put("status", HttpStatus.BAD_REQUEST.value());
            errors.put("error", HttpStatus.BAD_REQUEST.toString());
            errors.put("mensagem", "Erro de validação desconhecido.");
            errors.put("timestamp", LocalDateTime.now());
            errors.put("path", request.getRequestURI());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ResponseEntity.status(400)
            .body(ErrorMapper.toResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                "Erro na leitura dos dados da requisição. Verifique o formato e os valores informados.",
                request.getRequestURI()));
    }

    @ExceptionHandler(InvalidFilterException.class)
    public ResponseEntity<?> handleInvalidFiler(InvalidFilterException ex, HttpServletRequest request) {
        return ResponseEntity.status(400)
            .body(ErrorMapper.toResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(AddressDoesNotBelongToPersonException.class)
    public ResponseEntity<?> AddressDoesNotBelongToPersonFiler(AddressDoesNotBelongToPersonException ex, HttpServletRequest request) {
        return ResponseEntity.status(400)
            .body(ErrorMapper.toResponse(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(404)
            .body(ErrorMapper.toResponse(HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(EntityNotSavedException.class)
    public ResponseEntity<?> handleEntityNotSaved(EntityNotSavedException ex, HttpServletRequest request) {
        return ResponseEntity.status(500)
            .body(ErrorMapper.toResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<?> handleEntityAlreadyExists(EntityAlreadyExistsException ex, HttpServletRequest request) {
        return ResponseEntity.status(409)
            .body(ErrorMapper.toResponse(HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.toString(),
                ex.getMessage(),
                request.getRequestURI()));
    }
}
