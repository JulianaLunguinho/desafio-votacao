package com.db.voting.service;

import com.db.voting.domain.Question;
import com.db.voting.domain.dto.CreateQuestionRequest;
import com.db.voting.domain.dto.CreateQuestionResponse;
import com.db.voting.domain.dto.OpenSessionResponse;
import com.db.voting.repositories.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public CreateQuestionResponse createQuestion(CreateQuestionRequest createQuestionRequest) {
        var question = Question.builder()
                .content(createQuestionRequest.getQuestion())
                .build();

        log.info("[CreateQuestionService] - Saving question: {}", question);
        var questionId = questionRepository.save(question).getId();
        log.info("[CreateQuestionService] - Question saved with id: {}", questionId);

        return new CreateQuestionResponse(questionId);

    }

    public OpenSessionResponse openSession(Long questionId, Integer duration) {

        log.info("[CreateQuestionService] - Getting question for id: {}", questionId);
        var optionalQuestion = questionRepository.findById(questionId);

        if(optionalQuestion.isEmpty())
            throw new EntityNotFoundException("Question not found");

        var question = optionalQuestion.get();

        if(!ObjectUtils.isEmpty(question.getInitialDatetime())) {
            var message = "The voting session for this question has already started";

            if(question.getEndDatetime().isBefore(LocalDateTime.now()))
                message = "The voting session for this question has now ended";

            throw new IllegalArgumentException(message);
        }

        var initial = LocalDateTime.now();
        var end = duration == null || duration < 1 ? initial.plusMinutes(1) : initial.plusMinutes(duration);

        question.setInitialDatetime(initial);
        question.setEndDatetime(end);

        log.info("[CreateQuestionService] - Saving question: {}", question);
        questionRepository.save(question);
        log.info("[CreateQuestionService] - Question saved successfully. Session open.");

        return OpenSessionResponse.builder()
                .questionId(questionId)
                .question(question.getContent())
                .sessionStart(question.getInitialDatetime().toString())
                .sessionEnd(question.getEndDatetime().toString())
                .build();

    }

}
