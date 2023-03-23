package com.api.assemblyvoter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "AgendaTable")
@Data
public class AgendaEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ElementCollection
    @CollectionTable(name = "associateVotesMapping", joinColumns = @JoinColumn(name = "agenda_id"))
    @MapKeyColumn(name = "associate_id")
    @Column(name = "associate_vote")
    private Map<Long, String> associateVotes = new HashMap<Long, String>();

}
