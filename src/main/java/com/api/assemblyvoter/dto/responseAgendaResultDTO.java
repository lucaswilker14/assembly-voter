package com.api.assemblyvoter.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class responseAgendaResultDTO implements Serializable {

    private String title;
    private int yesVotes;
    private int noVotes;
    private int totalVotes;

}
