package com.db.voting.domain.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class CreateQuestionRequest {

    @Valid
    @NotBlank
    private String question;

}
