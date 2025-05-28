package org.example.stellog.badge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badge")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Badge {
    @Id
    @Column(name = "badge_id")
    private Long id;

    private String name;

    @Builder
    private Badge(String name) {
        this.name = name;
    }
}
