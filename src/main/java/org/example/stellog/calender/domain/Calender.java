package org.example.stellog.calender.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stellog.global.entity.BaseEntity;
import org.example.stellog.member.domain.Member;
import org.example.stellog.room.domain.Room;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calender extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calender_id")
    private Long id;

    private String name;

    private String date;

    private boolean completed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Builder
    public Calender(String name, String date, boolean completed, Member member, Room room) {
        this.name = name;
        this.date = date;
        this.completed = completed;
        this.member = member;
        this.room = room;
    }

    public void completed() {
        this.completed = true;
    }

    public void cancelCompleted() {
        this.completed = false;
    }

    public void updateCalender(String name, String date) {
        this.name = name;
        this.date = date;
    }
}
