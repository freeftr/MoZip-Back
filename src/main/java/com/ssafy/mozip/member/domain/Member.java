package com.ssafy.mozip.member.domain;

import com.ssafy.mozip.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column(nullable = false, length = 256)
    private String profileImage;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false, length = 10)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, length = 1000)
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;
}
