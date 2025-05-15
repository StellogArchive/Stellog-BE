package org.example.stellog.review.domain.repository;

import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.ReviewMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewMemberRepository extends JpaRepository<ReviewMember, Long> {
    Optional<ReviewMember> findByReview(Review review);
}
