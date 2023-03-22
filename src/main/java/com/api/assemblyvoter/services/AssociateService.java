package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.VoteDTO;
import com.api.assemblyvoter.models.AssociateModel;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

public interface AssociateService {

    HttpStatus newAssociates(int quantity);

    List<AssociateModel> getAssociates();

    void deleteAll();

    HashMap<String, String> statusVote(String cpf);

    Object vote(VoteDTO voteDTO);

}
