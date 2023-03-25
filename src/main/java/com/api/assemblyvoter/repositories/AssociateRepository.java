package com.api.assemblyvoter.repositories;

import com.api.assemblyvoter.entity.AssociateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {

    AssociateEntity findAssociateByCpf(String cpf);
}
