package dev.ivushkin.sanctioned_name_matcher.exception;

import dev.ivushkin.sanctioned_name_matcher.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyNormalizedNameException.class)
    public ResponseEntity<ErrorResponse> emptyNormalizedNameException(EmptyNormalizedNameException ex) {
        log.warn("Normalization error: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(SanctionedNameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(SanctionedNameNotFoundException ex) {
        log.info("Not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NormalizedNameTooShortException.class)
    public ResponseEntity<ErrorResponse> handleNameTooShort(NormalizedNameTooShortException ex) {
        log.warn("Normalized name too short: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

}
