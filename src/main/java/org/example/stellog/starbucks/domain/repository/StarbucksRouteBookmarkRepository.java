package org.example.stellog.starbucks.domain.repository;

import org.example.stellog.member.domain.Member;
import org.example.stellog.starbucks.domain.StarbucksRoute;
import org.example.stellog.starbucks.domain.StarbucksRouteBookmark;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StarbucksRouteBookmarkRepository extends JpaRepository<StarbucksRouteBookmark, Long> {
    boolean existsByMemberAndStarbucksRoute(Member currentMember, StarbucksRoute route);

    Optional<StarbucksRouteBookmark> findByMemberAndStarbucksRoute(Member currentMember, StarbucksRoute route);

    List<StarbucksRouteBookmark> findAllByMember(Member member);

    @Query("SELECT b.starbucksRoute " +
            "FROM StarbucksRouteBookmark b " +
            "GROUP BY b.starbucksRoute " +
            "ORDER BY COUNT(b) DESC")
    List<StarbucksRoute> findTopRoutesByBookmarkCount(Pageable pageable);

    int countByStarbucksRoute(StarbucksRoute route);
}
