package com.db.voting.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidDataException extends RuntimeException {

    private final int code;

    public InvalidDataException(String message) {
        super(message);
        this.code = HttpStatus.BAD_REQUEST.value();
    }

}
