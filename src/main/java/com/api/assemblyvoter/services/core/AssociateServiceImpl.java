package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.VoteDTO;
import com.api.assemblyvoter.models.AssociateModel;
import com.api.assemblyvoter.repositories.AgendaRepository;
import com.api.assemblyvoter.repositories.AssociateRepository;
import com.api.assemblyvoter.services.AssociateService;
import com.api.assemblyvoter.utils.WebFluxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AssociateServiceImpl implements AssociateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateRepository associateRepository;

    private final AgendaRepository agendaRepository;

    private final WebFluxUtils webFluxUtils;

    @Autowired
    public AssociateServiceImpl(AssociateRepository associateRepository, AgendaRepository agendaRepository, WebFluxUtils webFluxUtils) {
        this.associateRepository = associateRepository;
        this.agendaRepository = agendaRepository;
        this.webFluxUtils = webFluxUtils;
    };

    @Override
    public HttpStatus newAssociates(int quantity) {
        return createAssociates(quantity);
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
    public Object vote(VoteDTO voteDTO) {
        String cpf = voteDTO.getAssociateCpf();
        String vote = voteDTO.getVote();
        Long agenda_id = Long.parseLong(voteDTO.getAgendaId());

        boolean canVote = webFluxUtils.canVote(cpf) && getAssociate(cpf).isPresent();

        if (canVote) {
            getAssociate(cpf).ifPresent(ass -> {
                ass.getAgendaVotes().put(getAgendaId(agenda_id), vote);
            });
        }

        return canVote;
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

    private Optional<AssociateModel> getAssociate(String cpf) {
        return Optional.ofNullable(associateRepository.findAssociateByCpf(cpf));
    }

    private Long getAgendaId(Long id) {
        return agendaRepository.findById(id).orElseThrow().getId();
    }

}
