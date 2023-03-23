package com.api.assemblyvoter.utils;

import com.api.assemblyvoter.enums.CanVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class WebClientUtils {

    private final WebClient webClient;

    @Autowired
    public WebClientUtils(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<String> generateCpfs(int quantity) {
        return Objects.requireNonNull(this.webClient.post()
                .uri("/cpf/generatelist?qtd=" + quantity)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block()
        );
    }

    public boolean canVote(String cpf) {
        var status = ableToVote(cpf);
        return status.get("status").equals(CanVote.ABLE_TO_VOTE.getStatus());
    }

    public HashMap<String, String> ableToVote(String cpf) {
        return Objects.requireNonNull(this.webClient.get()
                .uri("/cpf/validator/" + cpf)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HashMap<String, String>>() {})
                .block());
    }

}
