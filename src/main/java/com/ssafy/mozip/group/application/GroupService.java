package com.ssafy.mozip.group.application;

import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import com.ssafy.mozip.group.domain.Group;
import com.ssafy.mozip.group.domain.Participant;
import com.ssafy.mozip.group.domain.repository.GroupRepository;
import com.ssafy.mozip.group.domain.repository.ParticipantRepository;
import com.ssafy.mozip.group.dto.response.GroupDetailResponse;
import com.ssafy.mozip.group.dto.response.GroupListResponse;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createGroup(
            String name,
            Long leaderId,
            List<String> emails
    ){

        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        Group group = Group.of(name, leader.getId());
        groupRepository.save(group);

        List<Member> members = memberRepository.findByEmailIn(emails)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        for (Member member : members) {
            Participant participant = Participant.of(member, group);
            participantRepository.save(participant);
        }
    }

    public GroupDetailResponse readGroup (Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        Member leader = memberRepository.findById(group.getLeaderId())
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        return new GroupDetailResponse(group.getName(), leader.getName());
    }

    public GroupListResponse readGroups(Long memberId) {

        Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        List<Group> groups = participantRepository.findGroupsByParticipantId(memberId);

        List<GroupListResponse.GroupResponse> groupResponses = groups.stream()
                .map(group -> new GroupListResponse.GroupResponse(
                        group.getName(),
                        memberRepository.findById(group.getLeaderId())
                                .map(Member::getName)
                                .orElse("Unknown Leader")
                ))
                .collect(Collectors.toList());

        return new GroupListResponse(groupResponses);
    }

    public void updateGroup (
            Long groupId,
            Long memberId,
            String newName
    ) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        Long leaderId = group.getLeaderId();

        if (leaderId.equals(memberId)) {
            group.setName(newName);
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
            Long groupId,
            List<String> emails
    ) {

        List<Member> members = memberRepository.findByEmailIn(emails)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_GROUP));

        for (Member member : members) {
            Participant participant = Participant.of(member, group);
            participantRepository.save(participant);
        }
    }
}
