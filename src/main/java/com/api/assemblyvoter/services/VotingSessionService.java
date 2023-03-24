package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.dto.request.VotingSessionDTO;
import org.springframework.http.ResponseEntity;

public interface VotingSessionService {


    ResponseEntity<Object> createVotingSession(VotingSessionDTO sessionDTO);

    ResponseEntity<Object> openSession(VotingSessionDTO sessionDTO, Long id);

}
