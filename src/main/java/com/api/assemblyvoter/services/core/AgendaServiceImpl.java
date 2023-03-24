package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.request.AgendaDTO;
import com.api.assemblyvoter.dto.response.ResultVotingSessionResponseDTO;
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
    public ResultVotingSessionResponseDTO votingResult(Long id) {
        ResultVotingSessionResponseDTO resultDTO = new ResultVotingSessionResponseDTO();

        getAgenda(id).ifPresent(agenda -> {
            var stream = agenda.getAssociateVotes().entrySet().stream();
            int totalVotes = agenda.getAssociateVotes().size();
            int yesVotes  = getResultVoting(stream);
            int noVotes = totalVotes - yesVotes;

            resultDTO.setTitle(agenda.getTitle());
            resultDTO.setYesVotes(yesVotes);
            resultDTO.setNoVotes(noVotes);
            resultDTO.setTotalVotes(totalVotes);
        });
        return resultDTO;
    }

    public void updateAssociatesVotes(Long agendaId, Long associateId, String vote) {
        getAgenda(agendaId).ifPresent(agenda -> {
            agenda.getAssociateVotes().put(associateId, vote);
            agendaRepository.saveAndFlush(agenda);
        });
    }

    public void updateVotedAgenda(AgendaEntity agenda, boolean voted) {
        LOGGER.info("UPDATE AGENDA -> VOTED");
        agenda.setVoted(voted);
        agendaRepository.saveAndFlush(agenda);
    }

    private static int getResultVoting(Stream<Map.Entry<Long, String>> stream) {
        return stream.filter(yesResult -> yesResult.getValue().equalsIgnoreCase("YES"))
                .toList()
                .size();
    }

}
