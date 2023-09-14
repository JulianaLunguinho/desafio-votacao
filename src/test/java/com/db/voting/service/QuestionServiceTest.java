package com.db.voting.service;

import com.db.voting.domain.Associate;
import com.db.voting.domain.Question;
import com.db.voting.domain.dto.CreateQuestionRequest;
import com.db.voting.domain.enums.Vote;
import com.db.voting.exception.InvalidDataException;
import com.db.voting.repository.AssociateRepository;
import com.db.voting.repository.QuestionRepository;
import com.db.voting.repository.VotesRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private VotesRepository votesRepository;

    @Mock
    private AssociateRepository associateRepository;

    @InjectMocks
    private QuestionService questionService;

    @Spy
    private Question question;

    private final EasyRandom easy = new EasyRandom();

    @Test
    void shouldCreateQuestion() {
        var createQuestionRequest = easy.nextObject(CreateQuestionRequest.class);
        var question = easy.nextObject(Question.class);

        when(questionRepository.save(any())).thenReturn(question);
        assertDoesNotThrow(() -> questionService.createQuestion(createQuestionRequest));
    }

    @Test
    void shouldOpenSession() {
        var question = easy.nextObject(Question.class);
        question.setInitialDatetime(null);

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertDoesNotThrow(() -> questionService.openSession(123L, 60));
    }

    @Test
    void shouldOpenSessionWithOneMinute() {
        question = easy.nextObject(Question.class);
        question.setInitialDatetime(null);

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertDoesNotThrow(() -> questionService.openSession(123L, null));
        assertEquals(question.getInitialDatetime().plusMinutes(1), question.getEndDatetime());
    }

    @Test
    void shouldThrowBecauseStatusIsOpen() {
        var question = easy.nextObject(Question.class);
        question.setEndDatetime(LocalDateTime.now().plusMinutes(60));

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertThrows(InvalidDataException.class, () -> questionService.openSession(123L, 60));
    }

    @Test
    void shouldThrowBecauseStatusIsEnded() {
        var question = easy.nextObject(Question.class);
        question.setEndDatetime(LocalDateTime.now().minusMinutes(60));

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertThrows(InvalidDataException.class, () -> questionService.openSession(123L, 60));
    }

    @Test
    void shouldRegisterYesVote() {
        question = easy.nextObject(Question.class);
        question.setEndDatetime(LocalDateTime.now().plusMinutes(60));
        var associate = easy.nextObject(Associate.class);
        var positiveVotes = question.getPositiveVotes();

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        when(associateRepository.findById(any())).thenReturn(Optional.of(associate));
        when(votesRepository.countByAssociateAndQuestion(any(), any())).thenReturn(0);
        assertDoesNotThrow(() -> questionService.registerVote(123L, 123L, Vote.YES));
        assertEquals(positiveVotes + 1, question.getPositiveVotes());
    }

    @Test
    void shouldRegisterNoVote() {
        question = easy.nextObject(Question.class);
        question.setEndDatetime(LocalDateTime.now().plusMinutes(60));
        var associate = easy.nextObject(Associate.class);
        var negativeVotes = question.getNegativeVotes();

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        when(associateRepository.findById(any())).thenReturn(Optional.of(associate));
        when(votesRepository.countByAssociateAndQuestion(any(), any())).thenReturn(0);
        assertDoesNotThrow(() -> questionService.registerVote(123L, 123L, Vote.NO));
        assertEquals(negativeVotes + 1, question.getNegativeVotes());
    }

    @Test
    void shouldThrowBecauseSessionNotStarted() {
        question = easy.nextObject(Question.class);
        question.setInitialDatetime(null);

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertThrows(InvalidDataException.class, () -> questionService.registerVote(123L, 123L, Vote.NO));
    }

    @Test
    void shouldThrowBecauseSessionEnded() {
        question = easy.nextObject(Question.class);
        question.setEndDatetime(LocalDateTime.now().minusMinutes(60));

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertThrows(InvalidDataException.class, () -> questionService.registerVote(123L, 123L, Vote.NO));
    }

    @Test
    void shouldThrowBecauseAssociateHasAlreadyVoted() {
        question = easy.nextObject(Question.class);
        question.setEndDatetime(LocalDateTime.now().plusMinutes(60));
        var associate = easy.nextObject(Associate.class);

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        when(associateRepository.findById(any())).thenReturn(Optional.of(associate));
        when(votesRepository.countByAssociateAndQuestion(any(), any())).thenReturn(1);
        assertThrows(InvalidDataException.class, () -> questionService.registerVote(123L, 123L, Vote.NO));
    }

    @Test
    void shouldGetResultQuestion() {
        question = easy.nextObject(Question.class);

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertDoesNotThrow(() -> questionService.getResultQuestion(123L));
    }

    @Test
    void shouldThrowBecauseSessionForThisQuestionHasNotYetStarted() {
        question = easy.nextObject(Question.class);
        question.setInitialDatetime(null);

        when(questionRepository.findById(any())).thenReturn(Optional.of(question));
        assertThrows(InvalidDataException.class, () -> questionService.getResultQuestion(123L));
    }

}