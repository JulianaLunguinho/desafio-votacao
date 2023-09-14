package com.db.voting.controller;

import com.db.voting.domain.ErrorMessage;
import com.db.voting.exception.EntityNotFoundException;
import com.db.voting.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = InvalidDataException.class)
    public ResponseEntity<ErrorMessage> invalidDataException(InvalidDataException exception) {
        var message = ErrorMessage.builder()
                .code(exception.getCode())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundException(EntityNotFoundException exception) {
        var message = ErrorMessage.builder()
                .code(exception.getCode())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(exception.getCode()).body(message);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> genericException(Exception exception) {
        var message = ErrorMessage.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(exception.getClass().getSimpleName())
                .message(exception.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(message);
    }

}

