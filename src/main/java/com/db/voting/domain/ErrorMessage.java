package com.db.voting.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {
    private int code;
    private String error;
    private String message;
}
