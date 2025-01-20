package com.ssafy.mozip.group.domain.repository;

import com.ssafy.mozip.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g.name, m.name FROM Group g " +
            "JOIN g.participants p " +
            "JOIN Member m ON g.leaderId = m.id " +
            "WHERE p.member.id = :memberId")
    HashMap<String, String> findAllGroupsByMemberId(Long memberId);
}