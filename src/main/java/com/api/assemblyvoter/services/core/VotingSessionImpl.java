package com.api.assemblyvoter.services.core;


import com.api.assemblyvoter.dto.request.VotingSessionDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.dto.response.ResultVotingSessionResponseDTO;
import com.api.assemblyvoter.entity.AgendaEntity;
import com.api.assemblyvoter.entity.VotingSessionEntity;
import com.api.assemblyvoter.rabbitmq.Producer;
import com.api.assemblyvoter.repositories.VotingSessionRepository;
import com.api.assemblyvoter.services.VotingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class VotingSessionImpl implements VotingSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final VotingSessionRepository votingSessionRepository;

    private final AgendaServiceImpl agendaService;

    private Producer producer;

    @Value("${thread.default.session}")
    private int sessionDurationDefaultSec;

    public VotingSessionImpl(VotingSessionRepository votingSessionRepository, AgendaServiceImpl agendaService, Producer producer) {
        this.votingSessionRepository = votingSessionRepository;
        this.agendaService = agendaService;
        this.producer = producer;
    }

    @Override
    public ResponseEntity<Object> createVotingSession(VotingSessionDTO sessionDTO) {

        AgendaEntity agenda = validateAgenda(Long.parseLong(sessionDTO.getAgendaId()));

        VotingSessionEntity sessionEntity = new VotingSessionEntity();
        sessionEntity.setAgenda(agenda);

        if (!ObjectUtils.isEmpty(sessionDTO.getSessionDurationSec())) {
            sessionEntity.setSessionDurationSec(sessionDTO.getSessionDurationSec());
        }
        LOGGER.info("SAVE NEW VOTING SESSION");
        votingSessionRepository.saveAndFlush(sessionEntity);
        return ResponseHandler.generateResponse(sessionEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> openSession(VotingSessionDTO sessionDTO, Long sessionId) {

        AgendaEntity agenda = validateAgenda(Long.parseLong(sessionDTO.getAgendaId()));
        VotingSessionEntity sessionEntity = getVotingSession(sessionId);

        setSessionTime(sessionDTO, sessionEntity);
        sessionEntity.setOpen(true);
        votingSessionRepository.saveAndFlush(sessionEntity);

        try {
            openThreadVotingSession(agenda, sessionEntity, sessionEntity.getSessionDurationSec());

            ResultVotingSessionResponseDTO responseDTO = agendaService.votingResult(Long.parseLong(sessionDTO.getAgendaId()));

            LOGGER.info("INFORMING THE PLATFORM THE RESULT OF THE VOTING SESSION");
            producer.send(responseDTO, sessionEntity);

            LOGGER.info("RESPONSE VOTING RESULT");
            return ResponseHandler.generateResponse(responseDTO, HttpStatus.OK);
        }catch (ExecutionException | InterruptedException | TimeoutException e) {
            return ResponseHandler.generateResponse("TIMEOUT ERROR", HttpStatus.REQUEST_TIMEOUT);
        }
    }

    private void setSessionTime(VotingSessionDTO sessionDTO, VotingSessionEntity sessionEntity) {
        if (!ObjectUtils.isEmpty(sessionDTO.getSessionDurationSec())) {
            sessionEntity.setSessionDurationSec(sessionDTO.getSessionDurationSec());
        }else {
            sessionEntity.setSessionDurationSec(sessionDurationDefaultSec);
        }
    }

    public VotingSessionEntity getVotingSession(Long id) {
        return votingSessionRepository.findById(id)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Voting Session Not Found."));
    }

    private void openThreadVotingSession(AgendaEntity agendaVoted, VotingSessionEntity session, int time)
            throws ExecutionException, InterruptedException, TimeoutException {

        long timer = time * 1000L; //milliseconds
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> future = executor.submit(() -> {
            try {
                LOGGER.info("INIT TIME VOTING SESSION");
                Thread.sleep(timer);
                LOGGER.info("CLOSED SESSION");
                session.setOpen(false);
                votingSessionRepository.saveAndFlush(session);
                agendaService.updateVotedAgenda(agendaVoted, true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        future.get(timer + 2000L, TimeUnit.MILLISECONDS);
    }

    private AgendaEntity validateAgenda(Long id) {

        Supplier<Stream<Optional<AgendaEntity>>> streamSupplier = () -> Stream.of(agendaService.getAgenda(id));
        var x = streamSupplier.get().toList();

        AgendaEntity agenda = x.get(0).orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Agenda Not Found."));

        if (agenda.isVoted()) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND, "It is not possible to vote on the Agenda. " +
                    "Agenda has already been voted");
        }
        return agenda;
    }
}
