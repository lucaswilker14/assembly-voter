package com.api.assemblyvoter.repositories;

import com.api.assemblyvoter.models.AgendaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<AgendaModel, Long> {
}
