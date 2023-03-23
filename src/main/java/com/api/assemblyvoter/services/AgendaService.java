package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.request.AgendaDTO;
import com.api.assemblyvoter.dto.response.AgendaResponseDTO;
import com.api.assemblyvoter.models.AgendaModel;

import java.util.List;
import java.util.Optional;

public interface AgendaService {

    AgendaModel createNewAgenda(AgendaDTO agendaDTO);

    Optional<AgendaModel> getAgenda(Long id);

    List<AgendaModel> getAgendas();

    AgendaResponseDTO votingResult(Long id);

}
