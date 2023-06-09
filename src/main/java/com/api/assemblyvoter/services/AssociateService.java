package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.entity.AssociateEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface AssociateService {

    HttpStatus newAssociates(int quantity);

    Optional<AssociateEntity> getAssociate(String cpf);

    List<AssociateEntity> getAssociates();

    void deleteAll();

    HashMap<String, String> statusAssociateVote(String cpf);

    ResponseEntity<Object> registerVote(VoteDTO voteDTO);

}
