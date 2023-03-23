package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.request.AgendaDTO;
import com.api.assemblyvoter.dto.response.AgendaResponseDTO;
import com.api.assemblyvoter.entity.AgendaEntity;
import com.api.assemblyvoter.repositories.AgendaRepository;
import com.api.assemblyvoter.services.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AgendaServiceImpl implements AgendaService {


    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AgendaRepository agendaRepository;

    @Autowired
    public AgendaServiceImpl(AgendaRepository agendaRepository) {
        this.agendaRepository = agendaRepository;
    }

    @Override
    public AgendaEntity createNewAgenda(AgendaDTO agendaDTO) {
        LOGGER.info("Starting the creation of the new Agenda");
        AgendaEntity newAgenda = new AgendaEntity();
        BeanUtils.copyProperties(agendaDTO, newAgenda);
        newAgenda.setAssociateVotes(Collections.emptyMap());
        return agendaRepository.saveAndFlush(newAgenda);
    }

    @Override
    public Optional<AgendaEntity> getAgenda(Long id) {
        return Optional.ofNullable(agendaRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Agenda Not Found")));
    }

    @Override
    public List<AgendaEntity> getAgendas() {
        return agendaRepository.findAll();
    }

    @Override
    public AgendaResponseDTO votingResult(Long id) {
        AgendaResponseDTO resultDTO = new AgendaResponseDTO();

        agendaRepository.findById(id).ifPresent(agenda -> {
            var stream = agenda.getAssociateVotes().entrySet().stream();
            int yesVotes  = getResultVoting(stream, "yes");
            int noVotes = getResultVoting(stream, "no");
            int totalVotes = agenda.getAssociateVotes().size();

            resultDTO.setTitle(agenda.getTitle());
            resultDTO.setYesVotes(yesVotes);
            resultDTO.setNoVotes(noVotes);
            resultDTO.setTotalVotes(totalVotes);
        });
        return resultDTO;
    }

    public void updateAssociateVotes(Long agendaId, Long associateId, String vote) {
        getAgenda(agendaId).ifPresent(agenda -> {
            agenda.getAssociateVotes().put(associateId, vote);
            agendaRepository.saveAndFlush(agenda);
        });
    }

    private static int getResultVoting(Stream<Map.Entry<Long, String>> stream, String vote) {
        return stream.filter(yesResult -> yesResult.getValue().equalsIgnoreCase(vote)).toList().size();
    }

}
