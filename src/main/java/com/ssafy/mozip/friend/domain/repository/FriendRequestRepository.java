package com.ssafy.mozip.friend.domain.repository;

import com.ssafy.mozip.friend.domain.FriendRequest;
import com.ssafy.mozip.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findAllBySender(Member sender);
    List<FriendRequest> findAllByReceiver(Member receiver);
}
