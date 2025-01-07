package com.ssafy.mozip.group.presentation;

import com.ssafy.mozip.group.application.GroupService;
import com.ssafy.mozip.group.dto.GroupCreateRequest;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.oauth2.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/create")
    public void createGroup(
            GroupCreateRequest groupCreateRequest,
            @AuthUser Member member) {

        String name = groupCreateRequest.name();
        List<String> emails = groupCreateRequest.emails();

        Long leaderId = member.getId();

        groupService.createGroup(name, leaderId, emails);
    }
}
