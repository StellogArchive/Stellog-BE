package org.example.stellog.review.domain.repository;

import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.StarbucksReview;
import org.example.stellog.starbucks.domain.Starbucks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarbucksReviewRepository extends JpaRepository<StarbucksReview, Long> {
    List<StarbucksReview> findAllByStarbucks(Starbucks starbucks);

    Optional<StarbucksReview> findByReview(Review review);
}
