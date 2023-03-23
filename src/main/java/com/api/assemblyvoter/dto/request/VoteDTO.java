package com.api.assemblyvoter.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = 8901L;

    @NotBlank
    @Size(max = 11)
    private String associateCpf;

    @NotBlank
    @Size(max = 3)
    private String vote;

    @NotBlank
    private String agendaId;


}
