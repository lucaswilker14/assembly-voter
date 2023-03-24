package com.api.assemblyvoter.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VotingSessionDTO {

    @NotBlank
    private String agendaId;

    private int sessionDurationSec;

}
