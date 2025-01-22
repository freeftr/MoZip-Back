package com.ssafy.mozip.friend.dto.response;

import com.ssafy.mozip.friend.domain.FriendRequest;

import java.util.List;

public record FriendRequestListResponse(
        List<FriendRequest> friendRequests
) {
}
