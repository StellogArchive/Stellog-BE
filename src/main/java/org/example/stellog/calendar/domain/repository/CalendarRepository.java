package org.example.stellog.calendar.domain.repository;

import org.example.stellog.calendar.domain.Calendar;
import org.example.stellog.member.domain.Member;
import org.example.stellog.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findAllByDateAndMemberAndRoom(String date, Member member, Room room);

    @Query("SELECT c FROM Calendar c WHERE c.date LIKE :datePattern AND c.member = :member AND c.room = :room")
    List<Calendar> findAllByDateLikeAndMemberAndRoom(@Param("datePattern") String datePattern,
                                                     @Param("member") Member member,
                                                     @Param("room") Room room);
}
