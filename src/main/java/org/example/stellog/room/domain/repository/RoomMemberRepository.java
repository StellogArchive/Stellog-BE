package org.example.stellog.room.domain.repository;

import org.example.stellog.member.domain.Member;
import org.example.stellog.room.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {
    List<RoomMember> findByMember(Member member);
}
