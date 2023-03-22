package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.AgendaDTO;
import com.api.assemblyvoter.dto.responseAgendaResultDTO;
import com.api.assemblyvoter.models.AgendaModel;
import com.api.assemblyvoter.repositories.AgendaRepository;
import com.api.assemblyvoter.services.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AgendaServiceImpl implements AgendaService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private AgendaRepository agendaRepository;

    @Autowired
    public AgendaServiceImpl(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    @Override
    public AgendaModel createNewAgenda(AgendaDTO agendaDTO) {
        LOGGER.info("Starting the creation of the new Agenda");
        AgendaModel newAgenda = new AgendaModel();
        BeanUtils.copyProperties(agendaDTO, newAgenda);
        newAgenda.setAssociateVotes(Collections.emptySet());
        return agendaRepository.save(newAgenda);
    }

}
