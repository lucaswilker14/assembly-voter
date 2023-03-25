package com.api.assemblyvoter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class VoteDTO implements Serializable {

    private static final long serialVersionUID = 8901L;

    @NotBlank
    @Size(max = 11,  message = "CPF must be 11 digits")
    private String associateCpf;

    @NotBlank
    @Size(min = 2, max = 3,  message = "Vote must ne 'YES' or 'NO'")
    @Pattern(regexp = "^(YES|yes|no|NO)$", message = "Vote must ne 'YES' or 'NO'")
    private String vote;

    @NotBlank
    private String agendaId;

    @NotBlank
    private String sessionId;


}
