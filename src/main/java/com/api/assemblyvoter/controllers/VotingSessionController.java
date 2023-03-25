package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.request.VotingSessionDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.services.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController("VotingSessionController")
@RequestMapping(value = "voting-session")
@Tag(name = "Voting Session Endpoints")
public class VotingSessionController {

    private final VotingSessionService votingSessionService;

    @Autowired
    public VotingSessionController(VotingSessionService votingSessionService) {
        this.votingSessionService = votingSessionService;
    }

    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Create new Voting Session"),
            @ApiResponse(responseCode = "404",description = "Agenda not found"),
            @ApiResponse(responseCode = "404",description = "Voting Session Not Found.")
    })
    @Operation(summary = "Create new Voting Session.")
    public ResponseEntity<Object> createVotingSession(@RequestBody @Valid VotingSessionDTO sessionDTO) {
        try {
            return votingSessionService.createVotingSession(sessionDTO);
        }catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/open/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Open Session to vote"),
            @ApiResponse(responseCode = "404",description = "Agenda not found")
    })
    @Operation(summary = "Open new Session for voting of the Agenda")
    public ResponseEntity<Object> openAssemblyVoting(@RequestBody @Valid VotingSessionDTO sessionDTO,
                                                     @Parameter(description = "Session ID", example = "56") @PathVariable("id") String sessionId) {
        try {
            return votingSessionService.openSession(sessionDTO, Long.parseLong(sessionId));
        }catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }

}
