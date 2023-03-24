package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.request.VotingSessionDTO;
import org.springframework.http.ResponseEntity;

public interface VotingSessionService {
    ResponseEntity<Object> openSession(VotingSessionDTO sessionDTO);

}
