package com.db.voting.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenSessionResponse {

    private Long questionId;
    private String question;
    private String sessionStart;
    private String sessionEnd;

}
