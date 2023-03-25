package com.api.assemblyvoter.dto.rabbitDTO;

import com.api.assemblyvoter.dto.response.ResultVotingSessionResponseDTO;
import com.api.assemblyvoter.entity.VotingSessionEntity;
import lombok.Data;

@Data
public class EventDTO {

    private ResultVotingSessionResponseDTO resultVotingSessionResponseDTO;

    private VotingSessionEntity sessionEntity;

}
