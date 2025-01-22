package com.ssafy.mozip.friend.application;

import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import com.ssafy.mozip.friend.domain.FriendRequest;
import com.ssafy.mozip.friend.domain.FriendRequestStatus;
import com.ssafy.mozip.friend.domain.Friendship;
import com.ssafy.mozip.friend.domain.repository.FriendRequestRepository;
import com.ssafy.mozip.friend.domain.repository.FriendshipRepository;
import com.ssafy.mozip.friend.dto.request.FriendRequestRequest;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createFriendRequest(
            FriendRequestRequest friendRequestRequest,
            Member sender
    ) {

        Member receiver = memberRepository.findByEmail(friendRequestRequest.email())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        FriendRequest friendRequest = FriendRequest.of(sender, receiver, FriendRequestStatus.WAIT);
        friendRequestRepository.save(friendRequest);

        return friendRequest.getId();
    }

    public List<FriendRequest> findAllSenderFriendRequests(
            Member sender
    ) {

        List<FriendRequest> friendshipRequests = friendRequestRepository.findAllBySender(sender);

        return friendshipRequests;
    }

    public List<FriendRequest> findAllReceiverFriendRequests(
            Member receiver
    ) {
        List<FriendRequest> friendshipRequests = friendRequestRepository.findAllByReceiver(receiver);

        return friendshipRequests;
    }

    public void deleteFriendRequest(
            Long friendRequestId
    ) {

        friendRequestRepository.deleteById(friendRequestId);
    }

    @Transactional
    public Long acceptFriendRequest(
            Long friendRequestId
    ) {

        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_FRIEND_REQUEST));

        friendRequestRepository.deleteById(friendRequestId);

        Friendship friendship = Friendship.of(friendRequest.getSender(), friendRequest.getReceiver());

        friendshipRepository.save(friendship);

        return friendship.getId();
    }

    public void deleteFriendship(
            Long friendShipId
    ) {

        friendshipRepository.deleteById(friendShipId);
    }
}
