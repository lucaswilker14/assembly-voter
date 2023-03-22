package com.api.assemblyvoter.services.core;

import com.api.assemblyvoter.dto.VoteDTO;
import com.api.assemblyvoter.models.AssociateModel;
import com.api.assemblyvoter.repositories.AssociateRepository;
import com.api.assemblyvoter.services.AssociateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Service
public class AssociateServiceImpl implements AssociateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private final AssociateRepository associateRepository;

    private final WebClient webClient;

    @Autowired
    public AssociateServiceImpl(WebClient webClient, AssociateRepository associateRepository) {
        this.webClient = webClient;
        this.associateRepository = associateRepository;
    };

    @Override
    public HttpStatus newAssociates(int quantity) {
        return createAssociates(quantity);
    }

    @Override
    public String vote(VoteDTO voteDTO) {
        //checa se o cpf pode votar


        //se puder, cadastra o voto na tabela
        //retorna VOTO_COMPUTED

        //se nao puder, retorna error.
        return null;
    }

    private HttpStatus createAssociates(int quantity) {
        LOGGER.info("Accessing External API to create valid CPFs");
        List<String> newsAssociates = Objects.requireNonNull(this.webClient.post()
                .uri("/cpf/generatelist?qtd=" + quantity)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {}).block());
        return saveNewAssociates(newsAssociates);
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
