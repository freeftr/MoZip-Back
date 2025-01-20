package com.ssafy.mozip.group.dto.response;

import com.ssafy.mozip.member.domain.Member;

import java.util.List;

public record GroupDetailResponse (
        String groupName,
        String leaderName,
        List<Member> participants
) {
}
