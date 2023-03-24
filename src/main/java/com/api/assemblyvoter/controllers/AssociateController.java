package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.services.AssociateService;
import com.api.assemblyvoter.services.core.AgendaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Associate Endpoints")
@Validated
public class AssociateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateService associateService;

    @Autowired
    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Create associate"),
            @ApiResponse(responseCode = "400",description = "QTD must be greater than one"),
            @ApiResponse(responseCode = "500",description = "AWS Elastic Beanstalk dont response")
    })
    @Operation(summary = "Create new Associate")
        public ResponseEntity<Object> createNewAssociates(@Parameter(description = "Quantity of new Associates will be created", example = "10")
                                                      @RequestParam @Min(value = 1, message = "QTD must be greater than one") Integer qtd) {
        associateService.newAssociates(qtd);
        return ResponseHandler.generateResponse("Created Associates", HttpStatus.CREATED);
    }

    @GetMapping("/{cpf}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Create Associate"),
            @ApiResponse(responseCode = "400",description = "CPF must be 11 digits"),
            @ApiResponse(responseCode = "404",description = "No Associate Registered."),
    })
    @Operation(summary = "Return Associate")
    public ResponseEntity<Object> getAssociate(@Parameter(description = "Associate CPF", example = "18735520198")
                                                   @PathVariable("cpf") @NotBlank
                                                   @Size(min = 11, max = 11, message = "CPF must be 11 digits") String cpf) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(associateService.getAssociate(cpf));
        } catch (HttpServerErrorException e) {
            LOGGER.info("No data Associates");
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Return all Registers Associates"),
            @ApiResponse(responseCode = "200",description = "No data Associates")
    })
    @Operation(summary = "Return All Associate")
    public ResponseEntity<Object> listAssociates() {
        return ResponseHandler.generateResponse(associateService.getAssociates(), HttpStatus.OK);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Delete All Associates")
    })
    @Operation(summary = "Delete All Associate")
    public ResponseEntity<Object> deleteAssociates() {
        associateService.deleteAll();
        return ResponseHandler.generateResponse("All Deleted Associated", HttpStatus.OK);
    }

    @GetMapping("/status/{cpf}")    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Associate Vote Status"),
            @ApiResponse(responseCode = "404",description = "Associate not found."),
    })
    @Operation(summary = "Return status vote Associate")
    public ResponseEntity<Object> statusAssociateVote(@Parameter(description = "Associate CPF", example = "18735520198")
                                                          @PathVariable("cpf") @NotBlank
                                                          @Size(min = 11, max = 11, message = "CPF must be 11 digits") String cpf) {
        try {
            return ResponseHandler.generateResponse(associateService.statusAssociateVote(cpf), HttpStatus.OK);
        } catch (WebClientResponseException e) {
            return ResponseHandler.generateResponse(String.format("%s %s", cpf, e.getStatusText()), HttpStatus.resolve(e.getStatusCode().value()));
        }
    }

    @PostMapping("/vote")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Vote Registered"),
            @ApiResponse(responseCode = "400",description = "Voting Session isn`t open."),
            @ApiResponse(responseCode = "400",description = "Associate can not vote"),
            @ApiResponse(responseCode = "404",description = "Voting Session Not Found."),
            @ApiResponse(responseCode = "404",description = "Agenda Not Found.")
    })
    @Operation(summary = "Register new associate`s vote")
    public ResponseEntity<Object> registerVote(@RequestBody @Valid VoteDTO voteDTO) {
        try {
            return ResponseHandler.generateResponse(associateService.registerVote(voteDTO), HttpStatus.OK);
        } catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.resolve(e.getStatusCode().value()));
        }
    }

}
