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

    List<Review> findAllByVisitedAtAndRoom(String visitedAt, Room room);

    @Query("SELECT r FROM Review r WHERE r.visitedAt LIKE :datePattern AND r.room = :room")
    List<Review> findAllByVisitedAtLikeAndRoom(@Param("datePattern") String datePattern,
                                               @Param("room") Room room);
}
