package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.services.AssociateService;
import com.api.assemblyvoter.services.core.AgendaServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController("AssociateController")
@RequestMapping(value = "associate")
public class AssociateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateService associateService;

    @Autowired
    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAssociates(@RequestParam int qtd) {
        associateService.newAssociates(qtd);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getAssociate(@PathVariable("cpf") String cpf) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(associateService.getAssociate(cpf));
        } catch (HttpServerErrorException e) {
            LOGGER.info("No data Associates");
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }


    @GetMapping()
    public ResponseEntity<Object> listAssociate() {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.getAssociates());
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public void deleteAssociates() {
        associateService.deleteAll();
    }

    @GetMapping("/status/{cpf}")
    public ResponseEntity<Object> statusVote(@PathVariable("cpf") String cpf) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(associateService.statusVote(cpf));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<String> vote(@RequestBody @Valid VoteDTO voteDTO) {
        try {
            return associateService.vote(voteDTO);
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getStatusText());
        }
    }

}
