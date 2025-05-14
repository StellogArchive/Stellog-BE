package org.example.stellog.room.domain.repository;

import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
