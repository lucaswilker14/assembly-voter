package com.api.assemblyvoter.entity;


import com.api.assemblyvoter.dto.response.ResultVotingSessionResponseDTO;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "VotingSessionTable")
@Data
public class VotingSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private AgendaEntity agenda;

    private boolean isOpen = false;

    private ResultVotingSessionResponseDTO votationResult;

    private int sessionDurationSec;

}

