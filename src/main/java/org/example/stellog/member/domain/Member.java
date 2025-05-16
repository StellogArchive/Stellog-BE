package org.example.stellog.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String nickName;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String provider;

    @Builder
    private Member(String name, String nickName, String email, UserRole userRole,
                   String provider) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.userRole = userRole;
        this.provider = provider;
    }
}
