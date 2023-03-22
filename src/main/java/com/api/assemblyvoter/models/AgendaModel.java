package com.api.assemblyvoter.models;

import jakarta.persistence.*;
import lombok.Data;

import javax.print.attribute.standard.MediaSize;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "AgendaTable")
@Data
public class AgendaModel implements Serializable {

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
