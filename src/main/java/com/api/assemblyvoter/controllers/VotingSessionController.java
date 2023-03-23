package com.api.assemblyvoter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("VotingSessionController")
@RequestMapping(value = "voting-session")
public class VotingSessionController {

    @PostMapping("/")
    public ResponseEntity<Object> openAssemblyVoting() {
        return ResponseEntity.status(HttpStatus.OK).body("OPEN VOTING OF SESSION");
    }

}
