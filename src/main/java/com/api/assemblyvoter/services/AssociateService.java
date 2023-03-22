package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.VoteDTO;
import org.springframework.http.HttpStatus;

public interface AssociateService {

    HttpStatus newAssociates(int quantity);

    String vote(VoteDTO voteDTO);

}
