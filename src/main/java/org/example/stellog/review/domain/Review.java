package org.example.stellog.review.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stellog.global.entity.BaseEntity;
import org.example.stellog.room.domain.Room;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String title;

    private String content;

    private String mainImgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Builder
    private Review(String title, String content, String mainImgUrl, Room room) {
        this.title = title;
        this.content = content;
        this.mainImgUrl = mainImgUrl;
        this.room = room;
    }

    public void updateReview(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
