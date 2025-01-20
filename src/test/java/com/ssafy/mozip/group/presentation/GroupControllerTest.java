package com.ssafy.mozip.group.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.mozip.group.application.GroupService;
import com.ssafy.mozip.group.dto.request.GroupCreateRequest;
import com.ssafy.mozip.group.dto.request.GroupUpdateRequest;
import com.ssafy.mozip.group.dto.response.GroupDetailResponse;
import com.ssafy.mozip.member.domain.Member;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import com.ssafy.mozip.common.controller.ControllerTest;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@WebMvcTest(GroupController.class)
class GroupControllerTest extends ControllerTest {

    @MockitoBean
    GroupService groupService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("모임을 생성할 수 있다.")
    public void testCreateGroup() throws Exception {

        Long groupId = 1L;
        var request = new GroupCreateRequest("Test Group", List.of("test1@email.com", "test2@email.com"));

        given(groupService.createGroup(any(), any()))
                .willReturn(groupId);

        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, "access-token")
                        .cookie(new Cookie("refresh-token", "refresh-token")))
                .andDo(print())
                .andExpect(status().isOk());

        verify(groupService).createGroup(any(), any());
    }

    @Test
    @DisplayName("모임 정보를 가져온다.")
    public void testReadGroup() throws Exception {
        Long groupId = 1L;

        List<Member> participants = List.of(
                Member.of("socialId1", "member1", "profileImage1", "member1@example.com"),
                Member.of("socialId2", "member2", "profileImage2", "member2@example.com")
        );

        GroupDetailResponse expect = new GroupDetailResponse(
                "Test Group",
                "Leader Name",
                participants
        );

        given(groupService.readGroup(any())).willReturn(expect);

        MvcResult result = mockMvc.perform(get("/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupName").value("Test Group"))
                .andExpect(jsonPath("$.leaderName").value("Leader Name"))
                .andExpect(jsonPath("$.participants[0].id").value(participants.get(0).getId()))
                .andExpect(jsonPath("$.participants[0].name").value("member1"))
                .andExpect(jsonPath("$.participants[1].id").value(participants.get(1).getId()))
                .andExpect(jsonPath("$.participants[1].name").value("member2"))
                .andReturn();
    }

    @Test
    @DisplayName("내가 속한 모임들의 목록을 볼 수 있다.")
    public void testReadGroups() throws Exception {
        var expect = new HashMap<String, String>();
        expect.put("group1", "Group One");
        expect.put("group2", "Group Two");

        given(groupService.readGroups(any(Member.class))).willReturn(expect);

        MvcResult result = mockMvc.perform(get("/groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.group1").value("Group One"))
                .andExpect(jsonPath("$.group2").value("Group Two"))
                .andReturn();

        verify(groupService, times(1)).readGroups(any(Member.class));
    }

    @Test
    @DisplayName("모임 정보를 업데이트 한다.")
    public void testUpdateGroup() throws Exception {
        Long groupId = 1L;
        var groupUpdateRequest = new GroupUpdateRequest("Updated Group Name");

        Member member = Member.of("socialId123", "John Doe", "profile.jpg", "john.doe@example.com");

        doNothing().when(groupService).updateGroup(eq(groupId), eq(member), eq(groupUpdateRequest));

        mockMvc.perform(patch("/groups/{groupId}", groupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupUpdateRequest)))
                .andExpect(status().isOk());

        verify(groupService, times(1)).updateGroup(eq(groupId), eq(member), eq(groupUpdateRequest));
    }


    @Test
    @DisplayName("모임을 삭제할 수 있다.")
    public void testDeleteGroup() throws Exception {
        Long groupId = 1L;

        doNothing().when(groupService).deleteGroup(eq(groupId));

        mockMvc.perform(delete("/groups/" + groupId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(groupService, times(1)).deleteGroup(eq(groupId));
    }
}