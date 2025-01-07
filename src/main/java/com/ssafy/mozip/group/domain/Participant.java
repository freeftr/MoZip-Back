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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
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
