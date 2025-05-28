package org.example.stellog.badge.domain.repository;

import org.example.stellog.badge.domain.RoomBadge;
import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBadgeRepository extends JpaRepository<RoomBadge, Long> {
    boolean existsByRoomAndBadgeId(Room room, Long badgeId);
}
