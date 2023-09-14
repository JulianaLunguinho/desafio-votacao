package com.db.voting.service;

import com.db.voting.domain.Question;
import com.db.voting.domain.Votes;
import com.db.voting.domain.dto.CreateQuestionRequest;
import com.db.voting.domain.dto.CreateQuestionResponse;
import com.db.voting.domain.dto.OpenSessionResponse;
import com.db.voting.domain.dto.QuestionResultResponse;
import com.db.voting.domain.enums.SessionStatus;
import com.db.voting.domain.enums.Vote;
import com.db.voting.exception.EntityNotFoundException;
import com.db.voting.exception.InvalidDataException;
import com.db.voting.repository.AssociateRepository;
import com.db.voting.repository.QuestionRepository;
import com.db.voting.repository.VotesRepository;
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

    @Autowired
    private VotesRepository votesRepository;

    @Autowired
    private AssociateRepository associateRepository;

    public CreateQuestionResponse createQuestion(CreateQuestionRequest createQuestionRequest) {
        var question = Question.builder()
                .content(createQuestionRequest.getQuestion())
                .build();

        log.info("[QuestionService] - Saving question: {}", question);
        var questionId = questionRepository.save(question).getId();
        log.info("[QuestionService] - Question saved with id: {}", questionId);

        return new CreateQuestionResponse(questionId);

    }

    public OpenSessionResponse openSession(Long questionId, Integer duration) {

        var question = getQuestionById(questionId);
        var status = getSessionStatus(question);

        if(!status.equals(SessionStatus.NOT_STARTED)) {
            if(status.equals(SessionStatus.OPEN))
                throw new InvalidDataException("The voting session for this question has already started");
            throw new InvalidDataException("The voting session for this question has now ended");
        }

        var initial = LocalDateTime.now();
        var end = (duration == null || duration < 1) ? initial.plusMinutes(1) : initial.plusMinutes(duration);

        question.setInitialDatetime(initial);
        question.setEndDatetime(end);

        log.info("[QuestionService] - Saving question: {}", question);
        questionRepository.save(question);
        log.info("[QuestionService] - Question saved successfully. Session open.");

        return OpenSessionResponse.builder()
                .questionId(questionId)
                .question(question.getContent())
                .sessionStart(question.getInitialDatetime().toString())
                .sessionEnd(question.getEndDatetime().toString())
                .build();

    }

    public void registerVote(Long questionId, Long associateId, Vote vote) {

        //verificar se sessão pra votação está aberta
        var question = getQuestionById(questionId);
        var status = getSessionStatus(question);

        if(!status.equals(SessionStatus.OPEN)) {
            if(status.equals(SessionStatus.NOT_STARTED))
                throw new InvalidDataException("The voting session for this question has not yet started");
            throw new InvalidDataException("The voting session for this question has now ended");
        }

        //verificar se usuário já votou nessa questão
        log.info("[QuestionService] - Getting associate for id: {}", associateId);
        var associate = associateRepository.findById(associateId)
                .orElseThrow(() -> new EntityNotFoundException("Associate not found"));
        var votesAssociate = votesRepository.countByAssociateAndQuestion(associate, question);
        if(votesAssociate != 0)
            throw new InvalidDataException("The associate has already voted on this question");

        //registrar voto na tabela de votos
        var votes = Votes.builder()
                .associate(associate)
                .question(question)
                .build();
        log.info("[QuestionService] - Saving vote {}", votes);
        votesRepository.save(votes);

        //somar voto na questão
        switch (vote) {
            case YES -> question.setPositiveVotes(question.getPositiveVotes() + 1);
            case NO -> question.setNegativeVotes(question.getNegativeVotes() + 1);
        }
        log.info("[QuestionService] - Saving question {}", question);
        questionRepository.save(question);

    }

    public QuestionResultResponse getResultQuestion(Long questionId) {
        var question = getQuestionById(questionId);
        var status = getSessionStatus(question);

        if(status.equals(SessionStatus.NOT_STARTED))
            throw new InvalidDataException("The voting session for this question has not yet started");

        var positiveVotes = question.getPositiveVotes();
        var negativeVotes = question.getNegativeVotes();
        var totalVotes = positiveVotes + negativeVotes;

        var positiveVotesPercent = positiveVotes * 100 / totalVotes;
        var negativeVotesPercent = negativeVotes * 100 / totalVotes;

        return QuestionResultResponse.builder()
                .questionId(questionId)
                .question(question.getContent())
                .sessionIsOpen(status.equals(SessionStatus.OPEN))
                .positiveVotes(positiveVotes)
                .positiveVotesPercent(positiveVotesPercent + "%")
                .negativeVotes(negativeVotes)
                .negativeVotesPercent(negativeVotesPercent + "%")
                .build();
    }

    private Question getQuestionById(Long questionId) {
        log.info("[QuestionService] - Getting question for id: {}", questionId);
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    private SessionStatus getSessionStatus(Question question) {
        if(ObjectUtils.isEmpty(question.getInitialDatetime()))
            return SessionStatus.NOT_STARTED;
        if(question.getEndDatetime().isBefore(LocalDateTime.now()))
            return SessionStatus.ENDED;
        return SessionStatus.OPEN;
    }

}
