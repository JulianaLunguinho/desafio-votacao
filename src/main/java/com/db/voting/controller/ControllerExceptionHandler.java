package com.db.voting.controller;

import com.db.voting.domain.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ResponseEntity<ErrorMessage> genericException(Exception exception) {
        var message = ErrorMessage.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(message);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> illegalArgumentException(IllegalArgumentException exception) {
        var message = ErrorMessage.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException exception) {
        var message = ErrorMessage.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(message);
    }
}
