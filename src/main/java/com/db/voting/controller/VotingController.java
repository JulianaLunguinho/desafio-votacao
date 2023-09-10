package com.db.voting.controller;

import com.db.voting.domain.dto.CreateQuestionRequest;
import com.db.voting.domain.dto.RegisterUserRequest;
import com.db.voting.service.QuestionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class VotingController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/question")
    public ResponseEntity createQuestion(@RequestBody CreateQuestionRequest request) {
        log.info("[VotingController] - {}", request);
        var response = questionService.createQuestion(request);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/session")
    public ResponseEntity openSession(@RequestParam Long questionId, @RequestParam(required = false) Integer duration) {
        log.info("[VotingController] - Request to open session for question: {}", questionId);
        var response = questionService.openSession(questionId, duration);
        return ResponseEntity.ok().body(response);
    }

    // Endpoint de votação

    // Get result

    // No cenário ideal, uma empresa teria o serviço para cadastro de usuário diferente do serviço para cadastro de pautas para votação.
    // No entanto, entendo que construir o serviço de cadastro de usuário fugiria ao objetivo desse teste.
    @PostMapping("/user")
    public void registerUser(@RequestBody RegisterUserRequest request) {
        return;
    }

}
