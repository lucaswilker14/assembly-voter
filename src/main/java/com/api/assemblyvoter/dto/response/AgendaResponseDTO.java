package com.api.assemblyvoter.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AgendaResponseDTO implements Serializable {

    private String title;

    private int yesVotes;

    private int noVotes;

    private int totalVotes;

}
