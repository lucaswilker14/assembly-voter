package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.request.AgendaDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.services.AgendaService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController("AgendaController")
@RequestMapping(value = "agenda")
@Tag(name = "Agenda Endpoints")
public class AgendaController {

    private AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @PostMapping()
    @ApiResponse(responseCode = "201",description = "Created new Pauta")
    @Operation(summary = "Create new Agenda to vote")
    public ResponseEntity<Object> createNewAgenda(@RequestBody @Valid AgendaDTO agendaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(agendaService.createNewAgenda(agendaDTO));
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "200",description = "Return all Agendas")
    @Operation(summary = "Return All Agenda registered in DB")
    public ResponseEntity<Object> listAgenda() {
        return ResponseEntity.status(HttpStatus.OK).body(agendaService.getAgendas());
    }

    @GetMapping("/{id}/result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Return result voting session"),
            @ApiResponse(responseCode = "404",description = "Agenda not found")
    })
    @Operation(summary = "Return Voting Result of the Agenda")
    public ResponseEntity<Object> votingResult(@Parameter(description = "Agenda ID") @PathVariable("id") Long id) {
        try {
            return ResponseHandler.generateResponse(agendaService.votingResult(id), HttpStatus.OK);
        }catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }
}
