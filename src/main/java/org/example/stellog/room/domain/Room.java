package org.example.stellog.room.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stellog.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;

    private boolean isPublic;

    @Builder
    private Room(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }

    public void updateRoom(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }
}
