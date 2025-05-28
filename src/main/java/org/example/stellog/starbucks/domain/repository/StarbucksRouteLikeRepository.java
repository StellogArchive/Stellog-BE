package org.example.stellog.starbucks.domain.repository;

import org.example.stellog.member.domain.Member;
import org.example.stellog.starbucks.domain.StarbucksRoute;
import org.example.stellog.starbucks.domain.StarbucksRouteLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StarbucksRouteLikeRepository extends JpaRepository<StarbucksRouteLike, Long> {
    boolean existsByMemberAndStarbucksRoute(Member member, StarbucksRoute starbucksRoute);

    Optional<StarbucksRouteLike> findByMemberAndStarbucksRoute(Member currentMember, StarbucksRoute route);
}
