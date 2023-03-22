package com.api.assemblyvoter.repositories;

import com.api.assemblyvoter.models.AssociateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<AssociateModel, Long> {
}
