package com.ssafy.mozip.friend.dto;

public record FriendRequestRequest(
        Long senderId,
        String email
) {
}
