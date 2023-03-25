package com.api.assemblyvoter.rabbitmq;

import com.api.assemblyvoter.dto.rabbitDTO.EventDTO;
import com.api.assemblyvoter.dto.response.ResultVotingSessionResponseDTO;
import com.api.assemblyvoter.entity.VotingSessionEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {


    private final RabbitTemplate rabbitTemplate;

    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void send(ResultVotingSessionResponseDTO dto, VotingSessionEntity session) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setResultVotingSessionResponseDTO(dto);
        eventDTO.setSessionEntity(session);
        rabbitTemplate.convertAndSend("session.v1.session-result", eventDTO);
    }
}
