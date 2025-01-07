package com.ssafy.mozip.member.domain;

import com.ssafy.mozip.common.domain.BaseTimeEntity;
import com.ssafy.mozip.group.domain.Participant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = true, length = 10)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean deleted;

    @OneToMany (mappedBy = "member", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    private Member(
            String socialId,
            String name,
            String profileImage,
            String email
    ) {
        this.socialId = socialId;
        this.name = name;
        this.profileImage = profileImage;
        this.email = email;
        this.role = Role.USER;
        this.status = Status.ACTIVE;
    }

    public static Member of(
            String socialId,
            String name,
            String profileImageUrl,
            String email
    ) {
        return new Member(
                socialId,
                name,
                profileImageUrl,
                email);
    }
}
