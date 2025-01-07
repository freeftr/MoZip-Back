package com.ssafy.mozip.group.domain;

import com.ssafy.mozip.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_group")
public class Group extends BaseTimeEntity {

    @Id
    @Column(name = "user_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long leaderId;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    private Group(
            String name,
            Long leaderId
    ) {
        this.name = name;
        this.leaderId = leaderId;
    }

    public static Group of(
            String name,
            Long leaderId
    ) {
        return new Group(
                name,
                leaderId);
    }
}
