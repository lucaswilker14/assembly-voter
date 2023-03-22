package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.VoteDTO;
import com.api.assemblyvoter.services.AssociateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController("AssociateController")
@RequestMapping(value = "associate")
public class AssociateController {

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
    public ResponseEntity<Object> statusVote(@PathVariable("cpf") String cpf ) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(associateService.statusVote(cpf));
        } catch (WebClientResponseException e ) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<Object> vote(@RequestBody @Valid VoteDTO voteDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(associateService.vote(voteDTO));
    }

}
