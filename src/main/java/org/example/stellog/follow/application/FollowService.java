package org.example.stellog.follow.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.follow.api.dto.response.FollowInfoResDto;
import org.example.stellog.follow.api.dto.response.FollowListResDto;
import org.example.stellog.follow.domain.Follow;
import org.example.stellog.follow.domain.repository.FollowRepository;
import org.example.stellog.follow.exception.AlreadyFollowingException;
import org.example.stellog.follow.exception.NotFollowingException;
import org.example.stellog.follow.exception.SelfFollowNotAllowedException;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {
    private final FollowRepository followRepository;
    private final MemberRoomService memberRoomService;

    @Transactional
    public void saveFollow(String email, Long memberId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Member followMember = memberRoomService.findMemberById(memberId);

        validateNotSelfFollow(currentMember, followMember);
        validateNotAlreadyFollowing(currentMember, followMember);

        Follow follow = Follow.builder()
                .follower(currentMember)
                .following(followMember)
                .build();
        followRepository.save(follow);
    }

    public FollowListResDto getFollows(String email) {
        Member currentMember = memberRoomService.findMemberByEmail(email);

        List<FollowInfoResDto> followDtoList = followRepository.findAllByFollower(currentMember)
                .stream()
                .map(Follow::getFollowing)
                .map(member -> new FollowInfoResDto(
                        member.getId(),
                        member.getName(),
                        member.getNickName(),
                        member.getProfileImgUrl()))
                .toList();

        return new FollowListResDto(followDtoList);
    }

    public FollowListResDto getFollowings(String email) {
        Member currentMember = memberRoomService.findMemberByEmail(email);

        List<FollowInfoResDto> followingDtoList = followRepository.findAllByFollowing(currentMember)
                .stream()
                .map(Follow::getFollower)
                .map(member -> new FollowInfoResDto(
                        member.getId(),
                        member.getName(),
                        member.getNickName(),
                        member.getProfileImgUrl()))
                .toList();

        return new FollowListResDto(followingDtoList);
    }

    @Transactional
    public void deleteFollow(String email, Long followId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Member followMember = memberRoomService.findMemberById(followId);
        validateNotSelfFollow(currentMember, followMember);
        Follow follow = findByFollowerAndFollowing(currentMember, followMember);
        followRepository.delete(follow);
    }

    private void validateNotAlreadyFollowing(Member currentMember, Member followMember) {
        if (followRepository.existsByFollowerAndFollowing(currentMember, followMember)) {
            throw new AlreadyFollowingException("이미 팔로우한 유저입니다.");
        }
    }

    private void validateNotSelfFollow(Member currentMember, Member followMember) {
        if (currentMember.getId().equals(followMember.getId())) {
            throw new SelfFollowNotAllowedException("자기 자신을 (언)팔로우할 수 없습니다.");
        }
    }

    private Follow findByFollowerAndFollowing(Member currentMember, Member followMember) {
        return followRepository.findByFollowerAndFollowing(currentMember, followMember)
                .orElseThrow(() -> new NotFollowingException("팔로우하지 않은 유저입니다."));
    }

    private void validateFollowExists(Member currentMember, Member followMember) {
        if (!followRepository.existsByFollowerAndFollowing(currentMember, followMember)) {
            throw new NotFollowingException("팔로우하지 않은 유저입니다.");
        }
    }
}
