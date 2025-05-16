package org.example.stellog.review.application;

import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomValidator;
import org.example.stellog.member.domain.Member;
import org.example.stellog.review.api.dto.request.ReviewRequestDto;
import org.example.stellog.review.api.dto.response.ReviewListResponseDto;
import org.example.stellog.review.api.dto.response.ReviewResponseDto;
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
import org.example.stellog.starbucks.StarbucksNotFoundException;
import org.example.stellog.starbucks.domain.Starbucks;
import org.example.stellog.starbucks.domain.repository.StarbucksRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final MemberRoomValidator memberRoomValidator;
    private final ReviewRepository reviewRepository;
    private final StarbucksRepository starbucksRepository;
    private final StarbucksReviewRepository starbucksReviewRepository;
    private final ReviewMemberRepository reviewMemberRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public void createReview(String email, Long roomId, ReviewRequestDto reviewRequestDto) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
        Room room = memberRoomValidator.findRoomById(roomId);
        memberRoomValidator.validateMemberInRoom(currentMember, room);
        Starbucks starbucks = findStarbucksById(reviewRequestDto.starbucksId());

        Review review = Review.builder()
                .title(reviewRequestDto.title())
                .content(reviewRequestDto.content())
                .room(room)
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

    public ReviewListResponseDto getAllReviewsByStarbucksId(String email, Long starbucksId) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
        Starbucks starbucks = findStarbucksById(starbucksId);

        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByStarbucks(starbucks);

        List<Review> reviews = starbucksReviews.stream()
                .map(StarbucksReview::getReview)
                .toList();
        Map<Long, ReviewMember> reviewToMemberMap = createReviewToReviewMemberMap(reviews);

        return getReviewListResponseDto(currentMember, starbucksReviews, reviews, reviewToMemberMap);
    }


    public ReviewListResponseDto getAllReviewsByRoomId(String email, Long roomId) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
        Room room = memberRoomValidator.findRoomById(roomId);
        memberRoomValidator.validateMemberInRoom(currentMember, room);

        List<Review> reviews = reviewRepository.findAllByRoom(room);
        Map<Long, ReviewMember> reviewToMemberMap = createReviewToReviewMemberMap(reviews);

        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByReviewIn(reviews);
        return getReviewListResponseDto(currentMember, starbucksReviews, reviews, reviewToMemberMap);
    }

    public ReviewListResponseDto getReviewsByRoomIdAndStarbucksId(String email, Long roomId, Long starbucksId) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
        Room room = memberRoomValidator.findRoomById(roomId);
        memberRoomValidator.validateMemberInRoom(currentMember, room);

        Starbucks starbucks = findStarbucksById(starbucksId);
        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByStarbucksAndRoom(starbucks, room);

        List<Review> reviews = starbucksReviews.stream()
                .map(StarbucksReview::getReview)
                .toList();
        Map<Long, ReviewMember> reviewToMemberMap = createReviewToReviewMemberMap(reviews);

        return getReviewListResponseDto(currentMember, starbucksReviews, reviews, reviewToMemberMap);
    }

    public ReviewResponseDto getReviewById(String email, Long reviewId) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        ReviewMember reviewMember = findReviewMemberByReview(review);
        StarbucksReview starbucksReview = findStarbucksReviewByReview(review);

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .starbucksId(starbucksReview.getStarbucks().getId())
                .title(review.getTitle())
                .content(review.getContent())
                .author(reviewMember.getMember().getName())
                .isLike(existsLikeByMemberAndReview(currentMember, review))
                .likeCount(countLikesByReview(review))
                .build();
    }

    @Transactional
    public void updateReview(String email, Long reviewId, ReviewRequestDto reviewRequestDto) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        ReviewMember reviewMember = findReviewMemberByReview(review);

        validateAuthorOfReview(currentMember, reviewMember);

        review.updateReview(reviewRequestDto.title(), reviewRequestDto.content());
    }

    @Transactional
    public void deleteReview(String email, Long reviewId) {
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
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
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
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
        Member currentMember = memberRoomValidator.findMemberByEmail(email);
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

    private ReviewListResponseDto getReviewListResponseDto(Member currentMember, List<StarbucksReview> starbucksReviews, List<Review> reviews, Map<Long, ReviewMember> reviewToMemberMap) {
        Map<Long, StarbucksReview> reviewToStarbucksReviewMap = createReviewToStarbucksReviewMap(starbucksReviews);
        Map<Long, Integer> likeCounts = countLikesByReviewIn(reviews);
        Set<Long> likedReviewIds = getLikedReviewsByMember(currentMember, reviews);

        List<ReviewResponseDto> reviewResponseDtos = getReviewResponseDtoList(
                reviews,
                reviewToMemberMap,
                reviewToStarbucksReviewMap,
                likedReviewIds,
                likeCounts
        );

        return new ReviewListResponseDto(reviewResponseDtos);
    }

    private List<ReviewResponseDto> getReviewResponseDtoList(List<Review> reviews,
                                                             Map<Long, ReviewMember> reviewToMemberMap,
                                                             Map<Long, StarbucksReview> reviewToStarbucksReviewMap,
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

                    return ReviewResponseDto.builder()
                            .reviewId(reviewId)
                            .starbucksId(starbucksReview.getStarbucks().getId())
                            .title(review.getTitle())
                            .content(review.getContent())
                            .author(reviewMember.getMember().getName())
                            .isLike(likedReviewIds.contains(reviewId))
                            .likeCount(likeCounts.getOrDefault(reviewId, 0))
                            .build();
                })
                .toList();
    }
}
