package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.request.AgendaDTO;
import com.api.assemblyvoter.dto.response.AgendaResponseDTO;
import com.api.assemblyvoter.models.AgendaModel;
import com.api.assemblyvoter.repositories.AgendaRepository;
import com.api.assemblyvoter.services.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AgendaServiceImpl implements AgendaService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AgendaRepository agendaRepository;

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
        return agendaRepository.saveAndFlush(newAgenda);
    }

    @Override
    public Optional<AgendaModel> getAgenda(Long id) {
        return Optional.ofNullable(agendaRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Agenda Not Found")));
    }

    @Override
    public List<AgendaModel> getAgendas() {
        return agendaRepository.findAll();
    }

    @Override
    public AgendaResponseDTO votingResult(Long id) {
        agendaRepository.findById(id).ifPresent(agendaModel -> {
            AgendaResponseDTO resultDTO = new AgendaResponseDTO();
            resultDTO.setTitle(agendaModel.getTitle());
            Long a = agendaModel.getAssociateVotes().entrySet().stream().filter(map -> map.getValue().equals("SIM")).count();
            LOGGER.info("TESTE");
        });
        return null;
    }

    public void updateAssociateVotes(Long agendaId, Long associateId, String vote) {
        getAgenda(agendaId).ifPresent(agenda -> {
            agenda.getAssociateVotes().put(associateId, StringUtils.capitalize(vote));
            agendaRepository.saveAndFlush(agenda);
        });
    }

}
