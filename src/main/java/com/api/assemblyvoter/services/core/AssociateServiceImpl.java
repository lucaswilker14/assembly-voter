package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.request.VoteDTO;
import com.api.assemblyvoter.models.AssociateModel;
import com.api.assemblyvoter.repositories.AssociateRepository;
import com.api.assemblyvoter.services.AssociateService;
import com.api.assemblyvoter.utils.WebFluxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AssociateServiceImpl implements AssociateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateRepository associateRepository;

    private final AgendaServiceImpl agendaService;

    private final WebFluxUtils webFluxUtils;

    @Autowired
    public AssociateServiceImpl(AssociateRepository associateRepository, AgendaServiceImpl agendaService,
                                WebFluxUtils webFluxUtils) {
        this.associateRepository = associateRepository;
        this.agendaService = agendaService;
        this.webFluxUtils = webFluxUtils;
    }

    @Override
    public HttpStatus newAssociates(int quantity) {
        return createAssociates(quantity);
    }

    @Override
    public Optional<AssociateModel> getAssociate(String cpf) {
        return Optional.of(Optional.ofNullable(associateRepository.findAssociateByCpf(cpf))
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Empty List")));
    }

    @Override
    public List<AssociateModel> getAssociates() {
        return associateRepository.findAll();
    }

    @Override
    public void deleteAll() {
        associateRepository.deleteAll();
    }

    @Override
    public HashMap<String, String> statusVote(String cpf) {
        return webFluxUtils.ableToVote(cpf);
    }

    @Override
    public ResponseEntity<String> vote(VoteDTO voteDTO) {
        String cpf = voteDTO.getAssociateCpf();
        Long agendaId = Long.parseLong(voteDTO.getAgendaId());

        boolean canVote = webFluxUtils.canVote(cpf) && getAssociate(cpf).isPresent();
        boolean voted = getAssociate(cpf).orElseThrow().getAgendaVotes().containsKey(agendaId);

        if (canVote && !voted) {
            getAssociate(cpf).ifPresent(ass -> {
                ass.getAgendaVotes().put(agendaService.getAgenda(agendaId)
                        .orElseThrow(() -> new HttpServerErrorException(HttpStatus.NOT_FOUND, "Associate Not Found"))
                        .getId(), StringUtils.capitalize(voteDTO.getVote()));
                associateRepository.saveAndFlush(ass);
                agendaService.updateAssociateVotes(agendaId, ass.getId(), voteDTO.getVote());
            });
        } else {
            throw new HttpServerErrorException(HttpStatus.NOT_ACCEPTABLE, "Associate has already voted for this Agenda");
        }

        return ResponseEntity.ok("Registered Vote");
    }

    private HttpStatus createAssociates(int quantity) {
        LOGGER.info("Accessing External API to create valid CPFs");
        return saveNewAssociates(webFluxUtils.generateCpfs(quantity));
    }

    private HttpStatus saveNewAssociates(List<String> associates) {
        associates.forEach(cpf -> {
            AssociateModel associateModel = new AssociateModel();
            associateModel.setCpf(cpf);
            associateRepository.save(associateModel);
            LOGGER.info("Save new Associate. ID Number -> {}", associateModel.getId());
        });
        return HttpStatus.CREATED;
    }
}
