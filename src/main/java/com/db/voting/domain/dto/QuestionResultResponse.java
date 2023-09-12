package com.db.voting.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResultResponse {
    private Long questionId;
    private String question;
    private Boolean sessionIsOpen;
    private Integer positiveVotes;
    private String positiveVotesPercent;
    private Integer negativeVotes;
    private String negativeVotesPercent;
}
