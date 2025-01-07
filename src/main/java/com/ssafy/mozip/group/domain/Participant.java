package com.ssafy.mozip.group.domain;

import com.ssafy.mozip.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private Participant(
            Member member,
            Group group
    ) {
        this.member = member;
        this.group = group;
    }

    public static Participant of(
            Member member,
            Group group
    ) {
        return new Participant(
                member,
                group);
    }
}
