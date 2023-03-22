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
import java.util.Map;

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
        newAgenda.setAssociateVotes(Collections.emptyMap());
        return agendaRepository.save(newAgenda);
    }

    @Override
    public responseAgendaResultDTO votingResult(Long id) {
        agendaRepository.findById(id).ifPresent(agendaModel -> {
            responseAgendaResultDTO resultDTO = new responseAgendaResultDTO();
            resultDTO.setTitle(agendaModel.getTitle());
            Long a = agendaModel.getAssociateVotes().entrySet().stream().filter(map -> map.getValue().equals("SIM")).count();
            LOGGER.info("TESTE");
        });
        return null;
    }

}
