package org.example.stellog.review.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomService;
import org.example.stellog.member.domain.Member;
import org.example.stellog.review.api.dto.request.ReviewReqDto;
import org.example.stellog.review.api.dto.response.ReviewInfoResDto;
import org.example.stellog.review.api.dto.response.ReviewListResDto;
import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.ReviewLike;
import org.example.stellog.review.domain.ReviewMember;
import org.example.stellog.review.domain.StarbucksReview;
import org.example.stellog.review.domain.repository.ReviewLikeRepository;
import org.example.stellog.review.domain.repository.ReviewMemberRepository;
import org.example.stellog.review.domain.repository.ReviewRepository;
import org.example.stellog.review.domain.repository.StarbucksReviewRepository;
import org.example.stellog.review.exception.DuplicateReviewLikeException;
import org.example.stellog.review.exception.ReviewMemberNotFoundException;
import org.example.stellog.review.exception.ReviewNotFoundException;
import org.example.stellog.review.exception.UnauthorizedReviewAccessException;
import org.example.stellog.room.domain.Room;
import org.example.stellog.starbucks.domain.Starbucks;
import org.example.stellog.starbucks.domain.repository.StarbucksRepository;
import org.example.stellog.starbucks.exception.StarbucksNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final MemberRoomService memberRoomService;
    private final ReviewRepository reviewRepository;
    private final StarbucksRepository starbucksRepository;
    private final StarbucksReviewRepository starbucksReviewRepository;
    private final ReviewMemberRepository reviewMemberRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public void createReview(String email, Long roomId, ReviewReqDto reviewReqDto) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(currentMember, room);
        Starbucks starbucks = findStarbucksById(reviewReqDto.starbucksId());

        Review review = Review.builder()
                .title(reviewReqDto.title())
                .content(reviewReqDto.content())
                .room(room)
                .mainImgUrl(reviewReqDto.mainImgUrl())
                .build();
        reviewRepository.save(review);

        ReviewMember reviewMember = ReviewMember.builder()
                .member(currentMember)
                .review(review)
                .build();
        reviewMemberRepository.save(reviewMember);

        StarbucksReview starbucksReview = StarbucksReview.builder()
                .review(review)
                .starbucks(starbucks)
                .build();
        starbucksReviewRepository.save(starbucksReview);
    }

    public ReviewListResDto getAllReviewsByStarbucksId(String email, Long starbucksId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Starbucks starbucks = findStarbucksById(starbucksId);

        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByStarbucks(starbucks);

        List<Review> reviews = starbucksReviews.stream()
                .map(StarbucksReview::getReview)
                .toList();
        Map<Long, ReviewMember> reviewToMemberMap = createReviewToReviewMemberMap(reviews);

        return getReviewListResponseDto(currentMember, starbucksReviews, reviews, reviewToMemberMap);
    }


    public ReviewListResDto getAllReviewsByRoomId(String email, Long roomId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(currentMember, room);

        List<Review> reviews = reviewRepository.findAllByRoom(room);
        Map<Long, ReviewMember> reviewToMemberMap = createReviewToReviewMemberMap(reviews);

        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByReviewIn(reviews);
        return getReviewListResponseDto(currentMember, starbucksReviews, reviews, reviewToMemberMap);
    }

    public ReviewListResDto getReviewsByRoomIdAndStarbucksId(String email, Long roomId, Long starbucksId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Room room = memberRoomService.findRoomById(roomId);
        memberRoomService.validateMemberInRoom(currentMember, room);

        Starbucks starbucks = findStarbucksById(starbucksId);
        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByStarbucksAndRoom(starbucks, room);

        List<Review> reviews = starbucksReviews.stream()
                .map(StarbucksReview::getReview)
                .toList();
        Map<Long, ReviewMember> reviewToMemberMap = createReviewToReviewMemberMap(reviews);

        return getReviewListResponseDto(currentMember, starbucksReviews, reviews, reviewToMemberMap);
    }

    public ReviewInfoResDto getReviewDetailById(String email, Long reviewId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        ReviewMember reviewMember = findReviewMemberByReview(review);
        StarbucksReview starbucksReview = findStarbucksReviewByReview(review);

        boolean isAuthor = currentMember.getId().equals(reviewMember.getMember().getId());

        return ReviewInfoResDto.builder()
                .id(review.getId())
                .starbucksId(starbucksReview.getStarbucks().getId())
                .title(review.getTitle())
                .content(review.getContent())
                .author(reviewMember.getMember().getName())
                .mainImgUrl(review.getMainImgUrl())
                .date(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .isAuthor(isAuthor)
                .isLike(existsLikeByMemberAndReview(currentMember, review))
                .likeCount(countLikesByReview(review))
                .build();
    }

    @Transactional
    public void updateReview(String email, Long reviewId, ReviewReqDto reviewReqDto) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        ReviewMember reviewMember = findReviewMemberByReview(review);

        validateAuthorOfReview(currentMember, reviewMember);

        review.updateReview(reviewReqDto.title(), reviewReqDto.content());
    }

    @Transactional
    public void deleteReview(String email, Long reviewId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        StarbucksReview starbucksReview = findStarbucksReviewByReview(review);
        ReviewMember reviewMember = findReviewMemberByReview(review);

        validateAuthorOfReview(currentMember, reviewMember);

        reviewLikeRepository.deleteAllByReview(review);
        reviewMemberRepository.delete(reviewMember);
        starbucksReviewRepository.delete(starbucksReview);
        reviewRepository.delete(review);
    }

    @Transactional
    public void likeReview(String email, Long reviewId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Review review = findReviewById(reviewId);

        if (reviewLikeRepository.existsByMemberAndReview(currentMember, review)) {
            throw new DuplicateReviewLikeException();
        }

        ReviewLike reviewLike = ReviewLike.builder()
                .member(currentMember)
                .review(review)
                .build();
        reviewLikeRepository.save(reviewLike);
    }

    @Transactional
    public void unlikeReview(String email, Long reviewId) {
        Member currentMember = memberRoomService.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        ReviewLike reviewLike = reviewLikeRepository.findByMemberAndReview(currentMember, review)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰의 좋아요 정보를 찾을 수 없습니다. reviewId: " + reviewId));

        reviewLikeRepository.delete(reviewLike);
    }


    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("해당 리뷰를 찾을 수 없습니다. reviewId: " + reviewId));
    }

    private ReviewMember findReviewMemberByReview(Review review) {
        return reviewMemberRepository.findByReview(review)
                .orElseThrow(() -> new ReviewMemberNotFoundException("리뷰 작성자 정보를 찾을 수 없습니다. reviewId: " + review.getId()));
    }

    private void validateAuthorOfReview(Member member, ReviewMember reviewMember) {
        if (!reviewMember.getMember().equals(member)) {
            throw new UnauthorizedReviewAccessException("본인이 작성한 리뷰만 수정/삭제할 수 있습니다. 현재 사용자: " + member.getId() + ", 리뷰 작성자: " + reviewMember.getMember().getId());
        }
    }

    private StarbucksReview findStarbucksReviewByReview(Review review) {
        return starbucksReviewRepository.findByReview(review)
                .orElseThrow(() -> new ReviewNotFoundException("해당 스타벅스 리뷰를 찾을 수 없습니다. reviewId: " + review.getId()));
    }

    private Starbucks findStarbucksById(Long starbucksId) {
        return starbucksRepository.findById(starbucksId)
                .orElseThrow(() -> new StarbucksNotFoundException("해당 스타벅스를 찾을 수 없습니다. starbucksId: " + starbucksId));
    }

    private boolean existsLikeByMemberAndReview(Member member, Review review) {
        return reviewLikeRepository.existsByMemberAndReview(member, review);
    }

    private int countLikesByReview(Review review) {
        return reviewLikeRepository.countByReview(review);
    }

    private Map<Long, Integer> countLikesByReviewIn(List<Review> reviews) {
        List<Object[]> counts = reviewLikeRepository.countLikesByReviewIn(reviews);
        return counts.stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0],
                        arr -> ((Long) arr[1]).intValue()
                ));
    }

    private Set<Long> getLikedReviewsByMember(Member member, List<Review> reviews) {
        List<ReviewLike> likedReviews = reviewLikeRepository.findAllByMemberAndReviewIn(member, reviews);
        return likedReviews.stream()
                .map(like -> like.getReview().getId())
                .collect(Collectors.toSet());
    }

    private Map<Long, StarbucksReview> createReviewToStarbucksReviewMap(List<StarbucksReview> starbucksReviews) {
        return starbucksReviews.stream()
                .collect(Collectors.toMap(sr -> sr.getReview().getId(), sr -> sr));
    }

    private Map<Long, ReviewMember> createReviewToReviewMemberMap(List<Review> reviews) {
        List<ReviewMember> reviewMembers = reviewMemberRepository.findAllByReviewIn(reviews);
        return reviewMembers.stream()
                .collect(Collectors.toMap(rm -> rm.getReview().getId(), rm -> rm));
    }

    private ReviewListResDto getReviewListResponseDto(Member currentMember, List<StarbucksReview> starbucksReviews, List<Review> reviews, Map<Long, ReviewMember> reviewToMemberMap) {
        Map<Long, StarbucksReview> reviewToStarbucksReviewMap = createReviewToStarbucksReviewMap(starbucksReviews);
        Map<Long, Integer> likeCounts = countLikesByReviewIn(reviews);
        Set<Long> likedReviewIds = getLikedReviewsByMember(currentMember, reviews);

        List<ReviewInfoResDto> reviewInfoResDtos = getReviewResponseDtoList(
                reviews,
                reviewToMemberMap,
                reviewToStarbucksReviewMap,
                currentMember,
                likedReviewIds,
                likeCounts
        );

        return new ReviewListResDto(reviewInfoResDtos);
    }

    private List<ReviewInfoResDto> getReviewResponseDtoList(List<Review> reviews,
                                                            Map<Long, ReviewMember> reviewToMemberMap,
                                                            Map<Long, StarbucksReview> reviewToStarbucksReviewMap,
                                                            Member currentMember,
                                                            Set<Long> likedReviewIds,
                                                            Map<Long, Integer> likeCounts) {
        return reviews.stream()
                .map(review -> {
                    Long reviewId = review.getId();

                    ReviewMember reviewMember = reviewToMemberMap.get(reviewId);
                    if (reviewMember == null) {
                        throw new ReviewMemberNotFoundException("리뷰 작성자 정보를 찾을 수 없습니다. reviewMemberId: " + reviewId);
                    }

                    StarbucksReview starbucksReview = reviewToStarbucksReviewMap.get(reviewId);
                    if (starbucksReview == null) {
                        throw new ReviewNotFoundException("리뷰에 연결된 스타벅스를 찾을 수 없습니다. reviewId: " + reviewId);
                    }

                    boolean isAuthor = reviewMember.getMember().getId().equals(currentMember.getId());

                    return ReviewInfoResDto.builder()
                            .id(reviewId)
                            .starbucksId(starbucksReview.getStarbucks().getId())
                            .title(review.getTitle())
                            .content(review.getContent())
                            .author(reviewMember.getMember().getName())
                            .date(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .mainImgUrl(review.getMainImgUrl())
                            .isAuthor(isAuthor)
                            .isLike(likedReviewIds.contains(reviewId))
                            .likeCount(likeCounts.getOrDefault(reviewId, 0))
                            .build();
                })
                .toList();
    }
}
