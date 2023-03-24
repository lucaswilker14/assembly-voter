package com.api.assemblyvoter.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.concurrent.TimeUnit;


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

    private int votationResult;

    private int sessionDuration;

}

