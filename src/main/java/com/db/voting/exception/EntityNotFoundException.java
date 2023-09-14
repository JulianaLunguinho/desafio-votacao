package com.db.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final int code;

    public EntityNotFoundException(String message) {
        super(message);
        this.code = HttpStatus.NOT_FOUND.value();
    }

}
