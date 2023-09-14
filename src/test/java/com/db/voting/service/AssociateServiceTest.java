package com.db.voting.service;

import com.db.voting.domain.Associate;
import com.db.voting.repository.AssociateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssociateServiceTest {

    @Mock
    private AssociateRepository associateRepository;

    @InjectMocks
    private AssociateService associateService;

    @Test
    void shouldRegisterAssociate() {
        when(associateRepository.save(any())).thenReturn(new Associate(12L, ""));
        assertDoesNotThrow(() -> associateService.registerAssociate("Joao testes"));
    }

}