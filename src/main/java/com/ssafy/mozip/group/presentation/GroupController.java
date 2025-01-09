package com.ssafy.mozip.group.presentation;

import com.ssafy.mozip.group.application.GroupService;
import com.ssafy.mozip.group.dto.AddParticipantsRequest;
import com.ssafy.mozip.group.dto.GroupCreateRequest;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.oauth2.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public void createGroup(
            @RequestBody GroupCreateRequest groupCreateRequest,
            @AuthUser Member member
    ) {

        String name = groupCreateRequest.name();
        List<String> emails = groupCreateRequest.emails();

        Long leaderId = member.getId();

        groupService.createGroup(name, leaderId, emails);
    }

    @PostMapping("/add")
    public void addParticipants(
            @RequestBody AddParticipantsRequest addParticipantsRequest,
            @AuthUser Member member
    ){

        Long groupId = addParticipantsRequest.groupId();
        List<String> emails = addParticipantsRequest.emails();

        groupService.addParticipants(groupId, emails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(
            @PathVariable Long id,
            @AuthUser Member member
    ) {

        groupService.deleteGroup(id);

        return ResponseEntity.ok().build();
    }
}
