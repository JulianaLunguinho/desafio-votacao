package com.db.voting.controller;

import com.db.voting.domain.dto.CreateQuestionRequest;
import com.db.voting.service.QuestionService;
import com.google.gson.Gson;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VotingControllerTest {

    @MockBean
    private QuestionService questionService;

    @InjectMocks
    private VotingController votingController;

    @Autowired
    private MockMvc mockMvc;

    private final Gson gson = new Gson();
    private final EasyRandom easy = new EasyRandom();

    @Test
    void shouldCreateQuestion() throws Exception {
        var request = easy.nextObject(CreateQuestionRequest.class);

        this.mockMvc.perform(post("/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseQuestionIsNull() throws Exception {
        this.mockMvc.perform(post("/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"question\": }"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseThereIsNoBodyRequest() throws Exception {
        this.mockMvc.perform(post("/question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void shouldOpenSession() throws Exception {
        this.mockMvc.perform(patch("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("questionId", "123")
                        .param("duration", "60"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseThereIsNoQuestionIdParam() throws Exception {
        this.mockMvc.perform(patch("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("duration", "60"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void shouldRegisterVote() throws Exception {
        this.mockMvc.perform(post("/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("questionId", "123")
                        .param("associateId", "123")
                        .param("vote", "YES"))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseThereIsNoQuestionParam() throws Exception {
        this.mockMvc.perform(post("/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("associateId", "123")
                        .param("vote", "YES"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseThereIsNoAssociateIdParam() throws Exception {
        this.mockMvc.perform(post("/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("questionId", "123")
                        .param("vote", "YES"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseThereIsNoVoteParam() throws Exception {
        this.mockMvc.perform(post("/vote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("questionId", "123")
                        .param("associateId", "123"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    void getResultQuestion() throws Exception {
        this.mockMvc.perform(get("/question/{id}/results", 123)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

}
