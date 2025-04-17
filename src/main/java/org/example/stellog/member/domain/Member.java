package org.example.stellog.member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickName;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String provider;

    private String providerId; // 소셜에서 주는 고유 ID

    @Builder
    public Member(String name, String nickName, String email, UserRole userRole,
        String provider, String providerId) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.userRole = userRole;
        this.provider = provider;
        this.providerId = providerId;
    }
}
