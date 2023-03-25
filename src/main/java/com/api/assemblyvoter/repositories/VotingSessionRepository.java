package com.api.assemblyvoter.repositories;

import com.api.assemblyvoter.entity.VotingSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingSessionRepository extends JpaRepository<VotingSessionEntity, Long> {
}
