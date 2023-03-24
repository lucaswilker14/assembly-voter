package com.api.assemblyvoter.rabbitmq;


import com.api.assemblyvoter.dto.rabbitDTO.EventDTO;
import com.api.assemblyvoter.repositories.VotingSessionRepository;
import com.api.assemblyvoter.services.core.AgendaServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgendaServiceImpl.class);

    private VotingSessionRepository sessionRepo;

    private Consumer(VotingSessionRepository sessionRepo) {
        this.sessionRepo = sessionRepo;
    }

    @RabbitListener(queues = "session.v1.session-result")
    public void receive(EventDTO eventDTO) {
        LOGGER.info("CONSUMER RECEIVER VOTING SESSION RESULT");
        LOGGER.info("{}", eventDTO);
        eventDTO.getSessionEntity().setVotationResult(eventDTO.getResultVotingSessionResponseDTO());
        sessionRepo.saveAndFlush(eventDTO.getSessionEntity());
        LOGGER.info("CONSUMER SAVING VOTING RESULT ON SESSION");
    }

}
