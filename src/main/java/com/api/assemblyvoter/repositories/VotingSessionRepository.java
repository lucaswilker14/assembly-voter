package com.api.assemblyvoter.repositories;

import com.api.assemblyvoter.models.VotingSessionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingSessionRepository extends JpaRepository<VotingSessionModel, Long> {
}
