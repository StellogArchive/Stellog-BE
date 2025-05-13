package org.example.stellog.room.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;

    private boolean isPublic;

//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RoomMember> roomMembers = new ArrayList<>();

    @Builder
    public Room(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }

    public void updateRoom(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }

//    public void addMemberRoom(RoomMember roomMember) {
//        this.roomMembers.add(roomMember);
//    }
}
