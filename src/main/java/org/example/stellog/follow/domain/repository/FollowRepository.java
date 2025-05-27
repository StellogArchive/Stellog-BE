package org.example.stellog.follow.domain.repository;

import org.example.stellog.follow.domain.Follow;
import org.example.stellog.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(Member member, Member followMember);

    List<Follow> findAllByFollower(Member currentMember);

    List<Follow> findAllByFollowing(Member currentMember);

    Optional<Follow> findByFollowerAndFollowing(Member currentMember, Member followMember);

    int countByFollower(Member member);

    int countByFollowing(Member member);
}
