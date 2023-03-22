package com.api.assemblyvoter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AgendaController")
@RequestMapping(value = "agenda")
public class AgendaController {

    @PostMapping("/")
    public ResponseEntity<Object> createNewAgenda() {
        return ResponseEntity.status(HttpStatus.OK).body("NEW AGENDA");
    }
}
