package com.api.assemblyvoter.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "AgendaTable")
@Data
public class AgendaModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(name = "agenda_associate", joinColumns = @JoinColumn(name = "agendas_fk")
                , inverseJoinColumns = @JoinColumn(name = "associate_fk"))
    private Set<AssociateModel> associateVotes;

}
