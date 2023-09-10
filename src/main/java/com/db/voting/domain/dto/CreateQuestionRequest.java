package com.db.voting.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateQuestionRequest {

    @NotBlank
    private String question;

}
