package com.api.assemblyvoter.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Entity
@Table(name = "AssociateTable")
@Data
public class AssociateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "vote")
    private String vote;

    @ElementCollection
    @CollectionTable(name = "agendaVotesMapping", joinColumns = @JoinColumn(name = "associate_id"))
    @MapKeyColumn(name = "agenda_id")
    @Column(name = "associate_vote")
    private Map<Long, String> agendaVotes;

}
