package com.db.voting.controller;

import com.db.voting.domain.dto.CreateQuestionRequest;
import com.db.voting.domain.dto.CreateQuestionResponse;
import com.db.voting.domain.dto.QuestionResultResponse;
import com.db.voting.domain.dto.OpenSessionResponse;
import com.db.voting.domain.enums.Vote;
import com.db.voting.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class VotingController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/question")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateQuestionResponse createQuestion(@RequestBody CreateQuestionRequest request) {
        log.info("[VotingController] - {}", request);
        return questionService.createQuestion(request);
    }

    @PatchMapping("/session")
    public ResponseEntity<OpenSessionResponse> openSession(@RequestParam Long questionId, @RequestParam(required = false) Integer duration) {
        log.info("[VotingController] - Request to open session for question: {}", questionId);
        var response = questionService.openSession(questionId, duration);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerVote(@RequestParam Long questionId, @RequestParam Long associateId, @RequestParam Vote vote) {
        log.info("[VotingController] - Request to register vote - questionId: {}, associateId: {}, vote: {}", questionId, associateId, vote);
        questionService.registerVote(questionId, associateId, vote);
    }

    @GetMapping("/question/{id}/results")
    public ResponseEntity<QuestionResultResponse> getResultQuestion(@PathVariable Long id) {
        log.info("[VotingController] - Request results of question {}", id);
        var response = questionService.getResultQuestion(id);
        return ResponseEntity.ok().body(response);
    }

}
