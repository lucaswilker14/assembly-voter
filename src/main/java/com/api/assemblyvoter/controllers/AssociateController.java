package com.api.assemblyvoter.controllers;

import com.api.assemblyvoter.services.AssociateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("AssociateController")
@RequestMapping(value = "associate")
public class AssociateController {

    private AssociateService associateService;

    @Autowired
    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewAssociates(@RequestParam int qtd) {
        associateService.newAssociates(qtd);
    }

}
