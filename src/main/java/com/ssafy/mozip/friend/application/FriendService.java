package com.ssafy.mozip.friend.application;

import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import com.ssafy.mozip.friend.domain.FriendRequest;
import com.ssafy.mozip.friend.domain.FriendRequestStatus;
import com.ssafy.mozip.friend.domain.repository.FriendRequestRepository;
import com.ssafy.mozip.friend.domain.repository.FriendshipRepository;
import com.ssafy.mozip.friend.dto.FriendRequestRequest;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createFriendRequest(
            FriendRequestRequest friendRequestRequest
    ) {

        Member sender = memberRepository.findById(friendRequestRequest.senderId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        Member receiver = memberRepository.findByEmail(friendRequestRequest.email())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        FriendRequest friendRequest = FriendRequest.of(sender, receiver, FriendRequestStatus.WAIT);
        friendRequestRepository.save(friendRequest);

        return friendRequest.getId();
    }
}
