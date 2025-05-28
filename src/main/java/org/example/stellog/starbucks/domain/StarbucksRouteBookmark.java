package org.example.stellog.starbucks.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stellog.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StarbucksRouteBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "starbucks_route_bookmark_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "starbucks_route_id")
    private StarbucksRoute starbucksRoute;

    @Builder
    private StarbucksRouteBookmark(Member member, StarbucksRoute starbucksRoute) {
        this.member = member;
        this.starbucksRoute = starbucksRoute;
    }
}
