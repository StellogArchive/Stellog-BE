package org.example.stellog.starbucks.domain.repository;

import org.example.stellog.room.domain.Room;
import org.example.stellog.starbucks.domain.StarbucksRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarbucksRouteRepository extends JpaRepository<StarbucksRoute, Long> {
    Optional<StarbucksRoute> findByRoom(Room room);

    List<StarbucksRoute> findAllByRoom(Room room);
}
