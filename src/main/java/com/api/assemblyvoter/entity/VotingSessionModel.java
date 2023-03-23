package com.api.assemblyvoter.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "VotingSessionTable")
@Data
public class VotingSessionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private AgendaEntity agenda;

    private int votes;

}

