package com.db.voting.controller;

import com.db.voting.service.AssociateService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AssociateControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AssociateService associateService;

    @InjectMocks
    private AssociateController associateController;

    @Test
    void shouldRegisterAssociate() throws Exception {
        this.mockMvc.perform(post("/associate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", "Joao Testes"))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void shouldThrowBecauseThereIsNoNameParam() throws Exception {
        this.mockMvc.perform(post("/associate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

}
