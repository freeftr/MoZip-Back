package com.ssafy.mozip.friend.domain.repository;

import com.ssafy.mozip.friend.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
