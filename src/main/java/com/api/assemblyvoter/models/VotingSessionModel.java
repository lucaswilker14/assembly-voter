package com.api.assemblyvoter.models;


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
    private AgendaModel agenda;

    private int votes;

}

