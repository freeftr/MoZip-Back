package com.ssafy.mozip.friend.domain;

import com.ssafy.mozip.common.domain.BaseTimeEntity;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus friendRequestStatus;

    private FriendRequest(
            Member sender,
            Member receiver,
            FriendRequestStatus friendRequestStatus
    ) {
        this.sender = sender;
        this.receiver = receiver;
        this.friendRequestStatus = friendRequestStatus;
    }

    public static FriendRequest of(
            Member sender,
            Member receiver,
            FriendRequestStatus friendRequestStatus) {
        return new FriendRequest(
                sender,
                receiver,
                friendRequestStatus
        );
    }
}
