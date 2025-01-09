package com.ssafy.mozip.group.presentation;

import com.ssafy.mozip.group.application.GroupService;
import com.ssafy.mozip.group.dto.request.AddParticipantsRequest;
import com.ssafy.mozip.group.dto.request.GroupCreateRequest;
import com.ssafy.mozip.group.dto.request.GroupUpdateRequest;
import com.ssafy.mozip.group.dto.response.GroupDetailResponse;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.oauth2.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<Void> createGroup(
            @RequestBody GroupCreateRequest groupCreateRequest,
            @AuthUser Member member
    ) {

        String name = groupCreateRequest.name();
        List<String> emails = groupCreateRequest.emails();

        Long leaderId = member.getId();

        groupService.createGroup(groupCreateRequest, member);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailResponse> readGroup(
            @PathVariable Long groupId,
            @AuthUser Member member
    ) {

        GroupDetailResponse groupDetailResponse = groupService.readGroup(groupId);

        return ResponseEntity.ok()
                .body(groupDetailResponse);
    }

    @GetMapping
    public ResponseEntity<HashMap<String, String>> readGroups(
            @AuthUser Member member
    ) {

        HashMap<String, String> groupInfos = groupService.readGroups(member);

        return ResponseEntity.ok()
                .body(groupInfos);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<Void> updateGroup(
            @PathVariable Long groupId,
            @RequestBody GroupUpdateRequest groupUpdateRequest,
            @AuthUser Member member
    ) {

        groupService.updateGroup(groupId, member, groupUpdateRequest);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Long id,
            @AuthUser Member member
    ) {

        groupService.deleteGroup(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addParticipants(
            @RequestBody AddParticipantsRequest addParticipantsRequest,
            @AuthUser Member member
    ){

        groupService.addParticipants(addParticipantsRequest);

        return ResponseEntity.ok().build();
    }

}
