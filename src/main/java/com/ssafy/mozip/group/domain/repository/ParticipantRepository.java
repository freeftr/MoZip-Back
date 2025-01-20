package com.ssafy.mozip.group.domain.repository;

import com.ssafy.mozip.group.domain.Participant;
import com.ssafy.mozip.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Long> findByGroupId(Long groupId);
}
