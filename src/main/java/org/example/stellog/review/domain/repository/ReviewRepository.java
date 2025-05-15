package org.example.stellog.review.domain.repository;

import org.example.stellog.review.domain.Review;
import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByRoom(Room room);
}
