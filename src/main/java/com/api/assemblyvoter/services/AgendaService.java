package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.request.AgendaDTO;
import com.api.assemblyvoter.dto.response.ResultVotingSessionResponseDTO;
import com.api.assemblyvoter.entity.AgendaEntity;

import java.util.List;
import java.util.Optional;

public interface AgendaService {

    AgendaEntity createNewAgenda(AgendaDTO agendaDTO);

    Optional<AgendaEntity> getAgenda(Long id);

    List<AgendaEntity> getAgendas();

    ResultVotingSessionResponseDTO votingResult(Long id);

}
