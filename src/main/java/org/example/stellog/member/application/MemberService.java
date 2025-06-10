package org.example.stellog.member.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.follow.domain.repository.FollowRepository;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.api.dto.request.MemberUpdateReqDto;
import org.example.stellog.member.api.dto.response.MemberInfoResDto;
import org.example.stellog.member.api.dto.response.MemberListResDto;
import org.example.stellog.member.domain.Member;
import org.example.stellog.member.domain.repository.MemberRepository;
import org.example.stellog.member.exception.MemberNotFoundException;
import org.example.stellog.review.domain.repository.ReviewMemberRepository;
import org.example.stellog.room.domain.repository.RoomMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoomService memberRoomService;
    private final ReviewMemberRepository reviewMemberRepository;
    private final FollowRepository followRepository;
    private final RoomMemberRepository roomMemberRepository;

    @Transactional
    public void updateMemberNickNme(String email, MemberUpdateReqDto memberUpdateReqDto) {
        Member member = memberRoomService.findMemberByEmail(email);
        member.updateNickName(memberUpdateReqDto.nickName());
    }

    @Transactional
    public void updateMemberProfileImg(String email, String profileImgUrl) {
        Member member = memberRoomService.findMemberByEmail(email);
        member.updateProfileImgUrl(profileImgUrl);
    }

    public MemberInfoResDto getMember(String email) {
        Member member = memberRoomService.findMemberByEmail(email);
        return createMemberInfoResDto(member);
    }

    public MemberInfoResDto getMemberById(String email, Long memberId) {
        Member member = memberRoomService.findMemberByEmail(email);
        Member targetMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. id: " + memberId));
        return createMemberInfoResDto(targetMember);
    }

    public MemberListResDto getAllMembers(String email, String name) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        List<Member> members = memberRepository.findByNameContaining(name);

        List<MemberInfoResDto> memberList = members.stream()
                .filter(m -> !m.getId().equals(currentMember.getId()))
                .map(this::createMemberInfoResDto)
                .toList();

        return new MemberListResDto(memberList);
    }

    private MemberInfoResDto createMemberInfoResDto(Member member) {
        int roomCount = roomMemberRepository.countByMember(member);
        int reviewCount = reviewMemberRepository.countByMember(member);
        int followingCount = followRepository.countByFollower(member);
        int followerCount = followRepository.countByFollowing(member);
        // TODO: 배지 리스트 추가

        return new MemberInfoResDto(
                member.getId(),
                member.getName(),
                member.getNickName(),
                member.getEmail(),
                member.getProfileImgUrl(),
                member.getProvider(),
                roomCount,
                reviewCount,
                followingCount,
                followerCount
        );
    }
}
