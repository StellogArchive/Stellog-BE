package org.example.stellog.review.domain.repository;

import org.example.stellog.member.domain.Member;
import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByMemberAndReview(Member member, Review review);

    boolean existsByMemberAndReview(Member member, Review review);

    int countByReview(Review review);

    List<ReviewLike> findAllByMemberAndReviewIn(Member currentMember, List<Review> reviews);

    @Query("SELECT r.id, COUNT(rl) FROM ReviewLike rl JOIN rl.review r WHERE r IN :reviews GROUP BY r.id")
    List<Object[]> countLikesByReviewIn(@Param("reviews") List<Review> reviews);

    @Modifying
    void deleteAllByReview(Review review);
}
