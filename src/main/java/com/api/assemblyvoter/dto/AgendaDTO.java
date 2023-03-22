package com.api.assemblyvoter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AgendaDTO {

    @NotBlank
    @Size(max = 500)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

}
