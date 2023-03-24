package com.api.assemblyvoter.services.core;


import com.api.assemblyvoter.dto.request.VotingSessionDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.entity.AgendaEntity;
import com.api.assemblyvoter.entity.VotingSessionEntity;
import com.api.assemblyvoter.repositories.VotingSessionRepository;
import com.api.assemblyvoter.services.VotingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.util.concurrent.*;

@Service
public class VotingSessionImpl implements VotingSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final VotingSessionRepository votingSessionRepository;

    private final AgendaServiceImpl agendaService;

    public VotingSessionImpl(VotingSessionRepository votingSessionRepository, AgendaServiceImpl agendaService) {
        this.votingSessionRepository = votingSessionRepository;
        this.agendaService = agendaService;
    }

    @Override
    public ResponseEntity<Object> openSession(VotingSessionDTO sessionDTO) {

        AgendaEntity agenda = agendaService.getAgenda(Long.parseLong(sessionDTO.getAgendaId()))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Agenda Not Found."));

        if (agenda.isVoted()) {
            return ResponseHandler.generateResponse("It is not possible to vote on the Agenda. Agenda has already been voted", HttpStatus.OK);
        }

        if (!ObjectUtils.isEmpty(sessionDTO.getSessionDuration())) {
            VotingSessionEntity entity = new VotingSessionEntity();
            entity.setAgenda(agenda);
            entity.setSessionDuration(sessionDTO.getSessionDuration());
            entity.setOpen(true);

            votingSessionRepository.saveAndFlush(entity);
            try {
                openVotingSession(agenda, entity, entity.getSessionDuration());
            }catch (ExecutionException | InterruptedException | TimeoutException e) {
                return ResponseHandler.generateResponse("TIMEOUT ERROR", HttpStatus.REQUEST_TIMEOUT);
            }
        }
        return ResponseHandler.generateResponse("", HttpStatus.OK);
    }

    private void openVotingSession(AgendaEntity agendaVoted, VotingSessionEntity session, int time) throws ExecutionException, InterruptedException, TimeoutException {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> future = executor.submit(() -> {
            try {
                LOGGER.info("INIT TIME SESSION");
                Thread.sleep(time * 1000L);
                LOGGER.info("CLOSED SESSION");
                session.setOpen(false);
                votingSessionRepository.saveAndFlush(session);
                agendaService.updateVoted(agendaVoted, true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        future.get(time*1000L, TimeUnit.SECONDS);
    }
}
