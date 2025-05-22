package org.example.stellog.global.util;

import lombok.RequiredArgsConstructor;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.domain.repository.MemberRepository;
import org.example.stellog.member.exception.MemberNotFoundException;
import org.example.stellog.room.domain.Room;
import org.example.stellog.room.domain.repository.RoomMemberRepository;
import org.example.stellog.room.domain.repository.RoomRepository;
import org.example.stellog.room.exception.RoomNotFoundException;
import org.example.stellog.room.exception.UnauthorizedRoomAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberRoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    public void validateMemberInRoom(Member member, Room room) {
        boolean isRoomMember = roomMemberRepository.existsByMemberAndRoom(member, room);
        if (!isRoomMember) {
            throw new UnauthorizedRoomAccessException("해당 방에 접근 권한이 없습니다.");
        }
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }
}
