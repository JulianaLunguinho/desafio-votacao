package com.db.voting.controller;

import com.db.voting.domain.dto.RegisterAssociateResponse;
import com.db.voting.service.AssociateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AssociateController {

    @Autowired
    private AssociateService associateService;

    @PostMapping("/associate")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterAssociateResponse registerAssociate(@RequestParam String name) {
        log.info("[AssociateController] - Request to register associate {}", name);
        return associateService.registerAssociate(name);
    }

}
