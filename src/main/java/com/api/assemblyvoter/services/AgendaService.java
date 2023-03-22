package com.api.assemblyvoter.services;

import com.api.assemblyvoter.dto.AgendaDTO;
import com.api.assemblyvoter.dto.responseAgendaResultDTO;
import com.api.assemblyvoter.models.AgendaModel;

public interface AgendaService {

    AgendaModel createNewAgenda(AgendaDTO agendaDTO);

    responseAgendaResultDTO votingResult(Long id);

}
