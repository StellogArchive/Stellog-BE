package org.example.stellog.review.domain.repository;

import org.example.stellog.review.domain.Review;
import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRoom(Room room);

    @Query("""
                SELECT COUNT(DISTINCT sr.starbucks.id)
                FROM Review r
                JOIN StarbucksReview sr ON r.id = sr.review.id
                WHERE r.room.id = :roomId
            """)
    long countDistinctStarbucksByRoomId(@Param("roomId") Long roomId);

    Review findByVisitedAtAndRoom(String visitedAt, Room room);

    List<Review> findAllByVisitedAtAndRoom(String visitedAt, Room room);
}
