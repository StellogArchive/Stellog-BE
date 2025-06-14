package org.example.stellog.calender.domain.repository;

import org.example.stellog.calender.domain.Calender;
import org.example.stellog.member.domain.Member;
import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender, Long> {
    Calender findByDateAndMemberAndRoom(String date, Member member, Room room);

    List<Calender> findAllByDateAndMemberAndRoom(String date, Member member, Room room);
}
