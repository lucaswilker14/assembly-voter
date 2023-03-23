package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.services.AssociateService;
import com.api.assemblyvoter.services.core.AgendaServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController("AssociateController")
@RequestMapping(value = "associate", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AssociateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateService associateService;

    @Autowired
    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping()
    public ResponseEntity<Object> createNewAssociates(@RequestParam @Min(value = 1, message = "QTD must be greater than one") Integer qtd) {
        associateService.newAssociates(qtd);
        return ResponseHandler.generateResponse("Created Associates", HttpStatus.CREATED);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Object> getAssociate(@PathVariable("cpf") @NotBlank
                                                   @Size(min = 11, max = 11, message = "CPF must be 11 digits") String cpf) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(associateService.getAssociate(cpf));
        } catch (HttpServerErrorException e) {
            LOGGER.info("No data Associates");
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping()
    public ResponseEntity<Object> listAssociates() {
        return ResponseHandler.generateResponse(associateService.getAssociates(), HttpStatus.OK);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteAssociates() {
        associateService.deleteAll();
        return ResponseHandler.generateResponse("All Deleted Associated", HttpStatus.OK);
    }

    @GetMapping("/status/{cpf}")
    public ResponseEntity<Object> statusAssociateVote(@PathVariable("cpf") @NotBlank
                                                          @Size(min = 11, max = 11, message = "CPF must be 11 digits") String cpf) {
        try {
            return ResponseHandler.generateResponse(associateService.statusAssociateVote(cpf), HttpStatus.OK);
        } catch (WebClientResponseException e) {
            return ResponseHandler.generateResponse(e.getResponseBodyAsString(), HttpStatus.resolve(e.getStatusCode().value()));
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<Object> registerVote(@RequestBody @Valid VoteDTO voteDTO) {
        try {
            return ResponseHandler.generateResponse(associateService.registerVote(voteDTO), HttpStatus.OK);
        } catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }

}
