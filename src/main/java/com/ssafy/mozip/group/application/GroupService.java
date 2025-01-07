package com.ssafy.mozip.group.application;

import com.ssafy.mozip.common.exception.BadRequestException;
import com.ssafy.mozip.common.exception.ExceptionCode;
import com.ssafy.mozip.group.domain.Group;
import com.ssafy.mozip.group.domain.Participant;
import com.ssafy.mozip.group.domain.repository.GroupRepository;
import com.ssafy.mozip.group.domain.repository.ParticipantRepository;
import com.ssafy.mozip.member.domain.Member;
import com.ssafy.mozip.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;

    public void createGroup(String name, Long leaderId, List<String> emails){

        Member leader = memberRepository.findById(leaderId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

        Group group = Group.of(name, leader.getId());
        groupRepository.save(group);

        for (String email : emails) {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_MEMBER));

            Participant participant = Participant.of(member, group);
            participantRepository.save(participant);
        }
    }
}
