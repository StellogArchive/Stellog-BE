package org.example.stellog.room.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.exception.MemberNotFoundException;
import org.example.stellog.member.repository.MemberRepository;
import org.example.stellog.room.api.dto.request.RoomRequestDto;
import org.example.stellog.room.api.dto.response.MemberInfoDto;
import org.example.stellog.room.api.dto.response.RoomDetailResponseDto;
import org.example.stellog.room.api.dto.response.RoomListResponseDto;
import org.example.stellog.room.api.dto.response.RoomResponseDto;
import org.example.stellog.room.domain.Room;
import org.example.stellog.room.domain.RoomMember;
import org.example.stellog.room.domain.repository.RoomMemberRepository;
import org.example.stellog.room.domain.repository.RoomRepository;
import org.example.stellog.room.exception.UnauthorizedRoomAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final MemberRoomService memberRoomService;

    @Transactional
    public void createRoom(String email, RoomRequestDto roomRequestDto) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        List<Member> selectedMembers = memberRepository.findAllById(roomRequestDto.memberIdList());

        if (!selectedMembers.contains(currentMember)) {
            selectedMembers.add(currentMember);
        }

        Room room = Room.builder()
                .name(roomRequestDto.name())
                .isPublic(roomRequestDto.isPublic())
                .build();
        roomRepository.save(room);

        for (Member member : selectedMembers) {
            boolean isOwner = member.getId().equals(currentMember.getId());
            RoomMember roomMember = RoomMember.builder()
                    .member(member)
                    .room(room)
                    .isOwner(isOwner)
                    .build();
            roomMemberRepository.save(roomMember);
        }
    }

    public RoomListResponseDto getAllRoom(String email) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        List<RoomMember> roomMembers = roomMemberRepository.findByMember(currentMember);

        List<RoomResponseDto> rooms = roomMembers.stream()
                .map(RoomMember::getRoom)
                .map(room -> {
                    int memberCount = roomMemberRepository.countByRoom(room);
                    return new RoomResponseDto(room.getId(), room.getName(), memberCount);
                })
                .toList();

        return new RoomListResponseDto(rooms);
    }

    public RoomDetailResponseDto getRoomDetails(String email, Long roomId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        List<RoomMember> roomMembers = findRoomMemberByRoom(room);

        memberRoomService.validateMemberInRoom(currentMember, room);

        List<MemberInfoDto> memberDtos = roomMembers.stream()
                .map(RoomMember::getMember)
                .map(MemberInfoDto::from)
                .toList();

        return new RoomDetailResponseDto(room.getId(), room.getName(), memberDtos);
    }

    @Transactional
    public void updateRoom(String email, Long roomId, RoomRequestDto roomRequestDto) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);

        checkRoomOwner(currentMember, room);

        room.updateRoom(roomRequestDto.name(), roomRequestDto.isPublic());
        updateRoomMembers(room, roomRequestDto.memberIdList(), currentMember);
    }

    @Transactional
    public void deleteRoom(String email, Long roomId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        List<RoomMember> roomMembers = findRoomMemberByRoom(room);

        checkRoomOwner(currentMember, room);

        roomMemberRepository.deleteAll(roomMembers);
        roomRepository.delete(room);
    }

    private List<RoomMember> findRoomMemberByRoom(Room room) {
        return roomMemberRepository.findByRoom(room);
    }

    private void checkRoomOwner(Member currentMember, Room room) {
        boolean isOwner = roomMemberRepository.existsByMemberAndRoomAndIsOwnerTrue(currentMember, room);
        if (!isOwner) {
            throw new UnauthorizedRoomAccessException("해당 방 수정 및 삭제 권한이 없습니다.");
        }
    }

    private void updateRoomMembers(Room room, List<Long> newMemberIds, Member currentMember) {
        Set<Long> distinctIds = new HashSet<>(newMemberIds);

        List<Member> foundMembers = memberRepository.findAllById(distinctIds);
        Set<Long> foundMemberIds = foundMembers.stream()
                .map(Member::getId)
                .collect(Collectors.toSet());

        if (foundMemberIds.size() != distinctIds.size()) {
            Set<Long> notFoundIds = new HashSet<>(distinctIds);
            notFoundIds.removeAll(foundMemberIds);
            throw new MemberNotFoundException("존재하지 않는 사용자 ID가 포함되어 있습니다." + notFoundIds);
        }

        if (!foundMembers.contains(currentMember)) {
            foundMembers.add(currentMember);
        }

        List<RoomMember> currentRoomMembers = roomMemberRepository.findByRoom(room);
        List<Member> currentMembers = currentRoomMembers.stream()
                .map(RoomMember::getMember)
                .toList();

        List<Member> toAdd = foundMembers.stream()
                .filter(m -> !currentMembers.contains(m))
                .toList();

        List<RoomMember> toRemove = currentRoomMembers.stream()
                .filter(rm -> !foundMembers.contains(rm.getMember()) && !rm.isOwner())
                .toList();

        removeRoomMembers(toRemove);
        addRoomMembers(room, toAdd, currentMember);
    }

    private void removeRoomMembers(List<RoomMember> toRemove) {
        roomMemberRepository.deleteAll(toRemove);
    }

    private void addRoomMembers(Room room, List<Member> toAdd, Member currentMember) {
        for (Member m : toAdd) {
            RoomMember newRm = RoomMember.builder()
                    .room(room)
                    .member(m)
                    .isOwner(m.equals(currentMember))
                    .build();
            roomMemberRepository.save(newRm);
        }
    }
}
