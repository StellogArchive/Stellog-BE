package org.example.stellog.review.domain.repository;

import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.StarbucksReview;
import org.example.stellog.room.domain.Room;
import org.example.stellog.starbucks.domain.Starbucks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StarbucksReviewRepository extends JpaRepository<StarbucksReview, Long> {
    List<StarbucksReview> findAllByStarbucks(Starbucks starbucks);

    Optional<StarbucksReview> findByReview(Review review);

    List<StarbucksReview> findAllByReviewIn(List<Review> reviews);

    @Query("SELECT sr FROM StarbucksReview sr WHERE sr.starbucks = :starbucks AND sr.review.room = :room")
    List<StarbucksReview> findAllByStarbucksAndRoom(@Param("starbucks") Starbucks starbucks, @Param("room") Room room);

}
