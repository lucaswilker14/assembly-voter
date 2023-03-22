package com.api.assemblyvoter.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "AssociateTable")
@Data
public class AssociateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private boolean vote;

    @Column
    private String cpf;

    @ManyToMany(mappedBy = "associateVotes")
    private Set<AgendaModel> agendaVotes;

}
