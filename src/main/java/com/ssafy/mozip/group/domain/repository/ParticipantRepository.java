package com.ssafy.mozip.group.domain.repository;

import com.ssafy.mozip.group.domain.Group;
import com.ssafy.mozip.group.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Group> findGroupsByParticipantId(Long id);
}
