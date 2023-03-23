package com.api.assemblyvoter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
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

    @ElementCollection
    @CollectionTable(name = "agenda_votes_mapping", joinColumns = @JoinColumn(name = "associate_id"))
    @MapKeyColumn(name = "agenda_id")
    @Column(name = "associate_vote")
    private Map<Long, String> agendaVotes = new HashMap<Long, String>();

}
