package com.ssafy.mozip.friend.dto.response;

import com.ssafy.mozip.member.domain.Member;

import java.util.List;

public record FriendListResponse(
        List<Member> friendList
) {
}
