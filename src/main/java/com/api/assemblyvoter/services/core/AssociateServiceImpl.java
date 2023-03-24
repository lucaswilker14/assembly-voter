package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.dto.response.ResponseHandler;
import com.api.assemblyvoter.entity.AssociateEntity;
import com.api.assemblyvoter.repositories.AssociateRepository;
import com.api.assemblyvoter.services.AssociateService;
import com.api.assemblyvoter.utils.WebClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AssociateServiceImpl implements AssociateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateRepository associateRepository;

    private final VotingSessionImpl session;

    private final AgendaServiceImpl agendaService;

    private final WebClientUtils webClientUtils;

    @Autowired
    public AssociateServiceImpl(AssociateRepository associateRepository, AgendaServiceImpl agendaService,
                                WebClientUtils webClientUtils, VotingSessionImpl votingSession) {
        this.associateRepository = associateRepository;
        this.agendaService = agendaService;
        this.webClientUtils = webClientUtils;
        this.session = votingSession;
    }

    @Override
    public HttpStatus newAssociates(int quantity) {
        return createAssociates(quantity);
    }

    @Override
    public Optional<AssociateEntity> getAssociate(String cpf) {
        return Optional.of(Optional.ofNullable(associateRepository.findAssociateByCpf(cpf))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "No Associate Registered.")));
    }

    @Override
    public List<AssociateEntity> getAssociates() {
        return associateRepository.findAll();
    }

    @Override
    public void deleteAll() {
        associateRepository.deleteAll();
    }

    @Override
    public HashMap<String, String> statusAssociateVote(String cpf) {
        return webClientUtils.ableToVote(cpf);
    }

    @Override
    public ResponseEntity<Object> registerVote(VoteDTO voteDTO) {

        String cpf = voteDTO.getAssociateCpf();
        Long agendaId = Long.parseLong(voteDTO.getAgendaId());
        boolean openedSession = session.getVotingSession(Long.parseLong(voteDTO.getSessionId())).isOpen();

        validateAssociate(openedSession, cpf, agendaId);

        getAssociate(cpf).ifPresent(ass -> {
            ass.getAgendaVotes().put(agendaService.getAgenda(agendaId)
                    .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Associate Not Found"))
                    .getId(), voteDTO.getVote().toUpperCase());
            associateRepository.saveAndFlush(ass);
            agendaService.updateAssociatesVotes(agendaId, ass.getId(), voteDTO.getVote().toUpperCase());
            LOGGER.info("CPF {} registered his vote", cpf);
        });
        return ResponseHandler.generateResponse("Registered Vote", HttpStatus.OK);
    }

    private void validateAssociate(boolean openedSession, String cpf, Long agendaId) {
        if (!openedSession) {
            throw new HttpServerErrorException(HttpStatus.NOT_FOUND, "Voting Session isn`t open.");
        }

        if (!isCanVote(cpf, agendaId)) {
            throw new HttpServerErrorException(HttpStatus.OK, "Associate can not vote");
        }
    }

    private boolean isCanVote(String cpf, Long agendaId) {
        try {
            boolean canVote = webClientUtils.canVote(cpf) && getAssociate(cpf).isPresent();
            boolean voted = getAssociate(cpf).orElseThrow().getAgendaVotes().containsKey(agendaId);
            return canVote && !voted;
        }catch (WebClientResponseException e) {
            throw new HttpServerErrorException(HttpStatus.OK, "Associate can not vote");
        }
    }

    private HttpStatus createAssociates(int quantity) {
        LOGGER.info("Accessing External API to create valid CPFs");
        return saveNewAssociates(webClientUtils.generateCpfs(quantity));
    }

    private HttpStatus saveNewAssociates(List<String> associates) {
        associates.forEach(cpf -> {
            AssociateEntity associateEntity = new AssociateEntity();
            associateEntity.setCpf(cpf);
            associateRepository.save(associateEntity);
            LOGGER.info("Save new Associate. ID Number -> {}", associateEntity.getId());
        });
        return HttpStatus.CREATED;
    }
}
