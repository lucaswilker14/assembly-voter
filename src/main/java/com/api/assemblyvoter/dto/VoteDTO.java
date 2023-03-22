package com.api.assemblyvoter.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = 8901L;

    private String associateCpf;
    private String vote;
    private String agendaId;


}
