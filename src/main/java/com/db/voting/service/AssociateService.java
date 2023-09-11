package com.db.voting.service;

import com.db.voting.domain.Associate;
import com.db.voting.domain.dto.RegisterAssociateResponse;
import com.db.voting.repositories.AssociateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AssociateService {

    @Autowired
    private AssociateRepository associateRepository;

    public RegisterAssociateResponse registerAssociate(String name) {
        var associate = Associate.builder().name(name).build();

        log.info("[AssociateService] - Saving associate {}", associate);
        var associatedId = associateRepository.save(associate).getId();
        log.info("[AssociateService] - Associate saved with id {}", associatedId);

        return new RegisterAssociateResponse(associatedId);
    }

}
