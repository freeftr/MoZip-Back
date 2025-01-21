package com.ssafy.mozip.friend.domain;

import com.ssafy.mozip.common.domain.BaseTimeEntity;
import com.ssafy.mozip.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friendship")
public class Friendship extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member friend1;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member friend2;
}
