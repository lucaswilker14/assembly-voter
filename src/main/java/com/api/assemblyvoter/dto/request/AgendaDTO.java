package com.api.assemblyvoter.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class AgendaDTO implements Serializable {

    @NotBlank
    @Size(max = 500)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

}
