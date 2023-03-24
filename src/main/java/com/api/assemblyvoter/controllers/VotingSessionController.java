package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.dto.request.VotingSessionDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.services.VotingSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

@RestController("VotingSessionController")
@RequestMapping(value = "voting-session")
public class VotingSessionController {

    private final VotingSessionService votingSessionService;

    @Autowired
    public VotingSessionController(VotingSessionService votingSessionService) {
        this.votingSessionService = votingSessionService;
    }

    @PostMapping()
    public ResponseEntity<Object> createVotingSession(@RequestBody @Valid VotingSessionDTO sessionDTO) {
        try {
            return votingSessionService.createVotingSession(sessionDTO);
        }catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/open/{id}")
    public ResponseEntity<Object> openAssemblyVoting(@RequestBody @Valid VotingSessionDTO sessionDTO,
                                                     @PathVariable("id") String sessionId) {
        try {
            return votingSessionService.openSession(sessionDTO, Long.parseLong(sessionId));
        }catch (HttpServerErrorException e) {
            return ResponseHandler.generateResponse(e.getStatusText(), HttpStatus.NOT_FOUND);
        }
    }

}
