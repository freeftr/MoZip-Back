package com.ssafy.mozip.friend.presentation;

import com.ssafy.mozip.friend.application.FriendService;
import com.ssafy.mozip.friend.dto.request.FriendRequestRequest;
import com.ssafy.mozip.friend.dto.request.FriendshipRequest;
import com.ssafy.mozip.friend.dto.response.FriendRequestListResponse;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.oauth2.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<Void> createFriendRequest(
            FriendRequestRequest friendRequestRequest,
            @AuthUser Member member
    ) {

        friendService.createFriendRequest(friendRequestRequest, member);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests")
    public ResponseEntity<FriendRequestListResponse> getSentRequestList(
            @AuthUser Member member
    ){

        FriendRequestListResponse friendRequestListResponse = new FriendRequestListResponse(friendService.findAllSenderFriendRequests(member));

        return ResponseEntity.ok()
                .body(friendRequestListResponse);
    }

    @GetMapping
    public ResponseEntity<FriendRequestListResponse> getReceivedRequests(
            @AuthUser Member member
    ) {

        FriendRequestListResponse friendRequestListResponse = new FriendRequestListResponse(friendService.findAllReceiverFriendRequests(member));

        return ResponseEntity.ok()
                .body(friendRequestListResponse);
    }

    @DeleteMapping("/{friendRequestId}")
    public ResponseEntity<Void> deleteFriendRequest(
            @PathVariable("friendRequestId") Long friendRequestId,
            @AuthUser Member member
    ) {

        friendService.deleteFriendRequest(friendRequestId);

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> acceptFriendRequest(
            FriendshipRequest friendshipRequest
    ) {

        friendService.acceptFriendRequest(friendshipRequest.friendshipId());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<Void> rejectFriendRequest(
            @PathVariable("friendshipId") Long friendshipId
    ) {

        friendService.deleteFriendship(friendshipId);

        return ResponseEntity.ok().build();
    }
}
