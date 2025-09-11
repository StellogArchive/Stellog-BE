package org.example.stellog.review.domain.repository;

import org.example.stellog.member.domain.Member;
import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.ReviewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewMemberRepository extends JpaRepository<ReviewMember, Long> {
    Optional<ReviewMember> findByReview(Review review);

    @Query("SELECT rm FROM ReviewMember rm WHERE rm.review IN :reviews")
    List<ReviewMember> findAllByReviewIn(@Param("reviews") List<Review> reviews);

    int countByMember(Member member);

    void deleteAllByReviewIn(List<Review> reviews);
}
