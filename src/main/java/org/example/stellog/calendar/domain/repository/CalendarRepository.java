package org.example.stellog.calendar.domain.repository;

import org.example.stellog.calendar.domain.Calendar;
import org.example.stellog.member.domain.Member;
import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Calendar findByDateAndMemberAndRoom(String date, Member member, Room room);

    List<Calendar> findAllByDateAndMemberAndRoom(String date, Member member, Room room);
}
