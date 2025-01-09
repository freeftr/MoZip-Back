package com.ssafy.mozip.group.application;

import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import com.ssafy.mozip.group.domain.Group;
import com.ssafy.mozip.group.domain.Participant;
import com.ssafy.mozip.group.domain.repository.GroupRepository;
import com.ssafy.mozip.group.domain.repository.ParticipantRepository;
import com.ssafy.mozip.group.dto.request.AddParticipantsRequest;
import com.ssafy.mozip.group.dto.request.GroupCreateRequest;
import com.ssafy.mozip.group.dto.request.GroupUpdateRequest;
import com.ssafy.mozip.group.dto.response.GroupDetailResponse;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createGroup(
            GroupCreateRequest groupCreateRequest,
            Member leader
    ){


        Group group = Group.of(
                groupCreateRequest.name(),
                leader.getId());

        groupRepository.save(group);

        List<Member> members = memberRepository.findByEmailIn(groupCreateRequest.emails())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        for (Member member : members) {
            Participant participant = Participant.of(member, group);
            participantRepository.save(participant);
        }

        Participant leaderParticipant = Participant.of(leader, group);
        participantRepository.save(leaderParticipant);
    }

    public GroupDetailResponse readGroup (Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        Member leader = memberRepository.findById(group.getLeaderId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        return new GroupDetailResponse(group.getName(), leader.getName());
    }

    @Transactional(readOnly = true)
    public HashMap<String, String> readGroups(Member member) {

        HashMap<String, String> groupInfos = groupRepository.findAllGroupsByMemberId(member.getId());

        return groupInfos;
    }

    public void updateGroup (
            Long groupId,
            Member member,
            GroupUpdateRequest groupUpdateRequest
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        if (group.isLeader(member.getId())) {
            group.updateName(groupUpdateRequest.newName());
            groupRepository.save(group);
        } else {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER);
        }
    }

    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        groupRepository.delete(group);
    }

    @Transactional
    public void addParticipants(
            AddParticipantsRequest addParticipantsRequest
    ) {

        List<Member> members = memberRepository.findByEmailIn(addParticipantsRequest.emails())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        Group group = groupRepository.findById(addParticipantsRequest.groupId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        for (Member member : members) {
            Participant participant = Participant.of(member, group);
            participantRepository.save(participant);
        }
    }
}
