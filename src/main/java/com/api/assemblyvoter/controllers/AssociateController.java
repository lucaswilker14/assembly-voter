package com.api.assemblyvoter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("AssociateController")
@RequestMapping(value = "associete")
public class AssociateController {

    @PostMapping("/")
    public ResponseEntity<Object> createNewAgenda() {
        return ResponseEntity.status(HttpStatus.OK).body("NEW ASSOCIATE");
    }
}
