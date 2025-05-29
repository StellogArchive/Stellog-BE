package org.example.stellog.badge.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stellog.badge.domain.Badge;
import org.example.stellog.badge.domain.RoomBadge;
import org.example.stellog.badge.domain.repository.BadgeRepository;
import org.example.stellog.badge.domain.repository.RoomBadgeRepository;
import org.example.stellog.badge.exception.BadgeNotFoundException;
import org.example.stellog.review.domain.repository.StarbucksReviewRepository;
import org.example.stellog.room.domain.Room;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final StarbucksReviewRepository starbucksReviewRepository;
    private final RoomBadgeRepository roomBadgeRepository;

    public void checkAndGrantBadgeByRoom(Room room) {
        long uniqueStarbucksCount = starbucksReviewRepository.countDistinctStarbucksByRoom(room);

        if (uniqueStarbucksCount >= 1) {
            grantBadge(room, 1L); // 첫 방문
        }
        if (uniqueStarbucksCount >= 5) {
            grantBadge(room, 2L); // 5곳
        }
        if (uniqueStarbucksCount >= 10) {
            grantBadge(room, 3L); // 10곳
        }
        if (uniqueStarbucksCount >= 20) {
            grantBadge(room, 4L); // 20곳
        }
        if (uniqueStarbucksCount >= 33) {
            grantBadge(room, 5L); // 전체 방문
        }
    }

//    public void checkAndGrantBadgeByRoom(StarbucksRoute starbucksRoute) {
//        long starbucksLikeCount = starbucksRouteLikeRepository.count(starbucksRoute);
//        Room room = starbucksRoute.getRoom();
//
//        if (starbucksLikeCount >= 100) {
//            grantBadge(room, 6L);
//        }
//    }

    private void grantBadge(Room room, Long badgeId) {
        log.info("Room {}: granted badge {}", room.getId(), badgeId);
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(BadgeNotFoundException::new);

        boolean alreadyGranted = roomBadgeRepository.existsByRoomAndBadgeId(room, badgeId);
        if (!alreadyGranted) {
            RoomBadge roomBadge = RoomBadge.builder()
                    .room(room)
                    .badge(badge)
                    .build();
            roomBadgeRepository.save(roomBadge);
        }
    }
}
