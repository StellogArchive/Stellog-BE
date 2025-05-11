package org.example.stellog.room.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.exception.MemberNotFoundException;
import org.example.stellog.member.repository.MemberRepository;
import org.example.stellog.room.api.dto.request.RoomRequestDto;
import org.example.stellog.room.api.dto.response.MemberSimpleDto;
import org.example.stellog.room.api.dto.response.RoomDetailResponseDto;
import org.example.stellog.room.api.dto.response.RoomResponseDto;
import org.example.stellog.room.domain.Room;
import org.example.stellog.room.domain.RoomMember;
import org.example.stellog.room.domain.repository.RoomMemberRepository;
import org.example.stellog.room.domain.repository.RoomRepository;
import org.example.stellog.room.exception.RoomNotFoundException;
import org.example.stellog.room.exception.UnauthorizedRoomAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Transactional
    public void createRoom(String email, RoomRequestDto roomRequestDto) {
        Member currentMember = findMemberByEmail(email);
        List<Member> selectedMembers = memberRepository.findAllById(roomRequestDto.members());
        if (!selectedMembers.contains(currentMember)) {
            selectedMembers.add(currentMember);
        }

        Room room = Room.builder()
                .name(roomRequestDto.name())
                .isPublic(roomRequestDto.isPublic())
                .build();
        roomRepository.save(room);

        for (Member member : selectedMembers) {
            boolean isOwner = member.equals(currentMember);
            RoomMember roomMember = RoomMember.builder()
                    .member(member)
                    .room(room)
                    .isOwner(isOwner)
                    .build();
            room.addMemberRoom(roomMember);
            roomMemberRepository.save(roomMember);
        }
    }

    public List<RoomResponseDto> getAllRoom(String email) {
        Member currentMember = findMemberByEmail(email);
        List<RoomMember> roomMembers = roomMemberRepository.findByMember(currentMember);

        return roomMembers.stream()
                .map(RoomMember::getRoom)
                .distinct()
                .map(room -> new RoomResponseDto(room.getId(), room.getName(), room.getRoomMembers().size()))
                .collect(Collectors.toList());
    }

    public RoomDetailResponseDto getRoomDetails(String email, Long roomId) {
        Member currentMember = findMemberByEmail(email);
        Room room = findRoomById(roomId);
        validateMemberInRoom(currentMember, room);

        List<MemberSimpleDto> memberDtos = room.getRoomMembers().stream()
                .map(RoomMember::getMember)
                .map(MemberSimpleDto::from)
                .toList();

        return new RoomDetailResponseDto(room.getId(), room.getName(), memberDtos);
    }

    @Transactional
    public void updateRoom(String email, Long roomId, RoomRequestDto roomRequestDto) {
        Member currentMember = findMemberByEmail(email);
        Room room = findRoomById(roomId);
        checkRoomOwner(currentMember, room);
        room.updateRoom(roomRequestDto.name(), roomRequestDto.isPublic());
        updateRoomMembers(room, roomRequestDto.members(), currentMember);
    }

    @Transactional
    public void deleteRoom(String email, Long roomId) {
        Member currentMember = findMemberByEmail(email);
        Room room = findRoomById(roomId);
        checkRoomOwner(currentMember, room);

        List<RoomMember> roomMembers = new ArrayList<>(room.getRoomMembers());
        roomMemberRepository.deleteAll(roomMembers);

        roomRepository.delete(room);
    }

    private Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }

    private void validateMemberInRoom(Member member, Room room) {
        boolean isRoomMember = room.getRoomMembers().stream()
                .anyMatch(roomMember -> roomMember.getMember().equals(member));
        if (!isRoomMember) {
            throw new UnauthorizedRoomAccessException("해당 방에 접근 권한이 없습니다.");
        }
    }

    private void checkRoomOwner(Member currentMember, Room room) {
        boolean isOwner = room.getRoomMembers().stream()
                .anyMatch(roomMember -> roomMember.getMember().equals(currentMember) && roomMember.isOwner());
        if (!isOwner) {
            throw new UnauthorizedRoomAccessException("해당 방 수정 권한이 없습니다.");
        }
    }

    private void updateRoomMembers(Room room, List<Long> newMemberIds, Member currentMember) {
        List<RoomMember> currentRoomMembers = new ArrayList<>(room.getRoomMembers());
        List<Member> currentMembers = currentRoomMembers.stream()
                .map(RoomMember::getMember)
                .toList();

        // 1. 새로운 멤버 목록 조회
        List<Member> updateMembers = memberRepository.findAllById(newMemberIds);

        // 2. 현재 멤버 목록에 현재 유저가 없으면 추가
        if (!updateMembers.contains(currentMember)) {
            updateMembers.add(currentMember);
        }

        // 3. 추가해야 할 멤버와 삭제해야 할 멤버 구분
        List<Member> toAdd = updateMembers.stream()
                .filter(m -> !currentMembers.contains(m))
                .toList();

        List<RoomMember> toRemove = currentRoomMembers.stream()
                .filter(rm -> !updateMembers.contains(rm.getMember()) && !rm.isOwner())
                .toList();

        // 4. 멤버 삭제 처리
        removeRoomMembers(room, toRemove);

        // 5. 멤버 추가 처리
        addRoomMembers(room, toAdd, currentMember);
    }

    private void removeRoomMembers(Room room, List<RoomMember> toRemove) {
        for (RoomMember rm : toRemove) {
            room.getRoomMembers().remove(rm);
            roomMemberRepository.delete(rm);
        }
    }

    private void addRoomMembers(Room room, List<Member> toAdd, Member currentMember) {
        for (Member m : toAdd) {
            RoomMember newRm = RoomMember.builder()
                    .room(room)
                    .member(m)
                    .isOwner(m.equals(currentMember))
                    .build();
            room.addMemberRoom(newRm);
            roomMemberRepository.save(newRm);
        }
    }
}
