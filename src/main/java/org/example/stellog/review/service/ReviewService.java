package org.example.stellog.review.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.stellog.global.util.MemberRoomValidator;
import org.example.stellog.member.domain.Member;
import org.example.stellog.review.api.dto.request.ReviewRequestDto;
import org.example.stellog.review.api.dto.respoonse.ReviewListResponseDto;
import org.example.stellog.review.api.dto.respoonse.ReviewResponseDto;
import org.example.stellog.review.domain.Review;
import org.example.stellog.review.domain.ReviewMember;
import org.example.stellog.review.domain.StarbucksReview;
import org.example.stellog.review.domain.repository.ReviewMemberRepository;
import org.example.stellog.review.domain.repository.ReviewRepository;
import org.example.stellog.review.domain.repository.StarbucksReviewRepository;
import org.example.stellog.review.exception.ReviewNotFoundException;
import org.example.stellog.review.exception.StarbucksReviewNotFoundException;
import org.example.stellog.review.exception.UnauthorizedReviewAccessException;
import org.example.stellog.room.domain.Room;
import org.example.stellog.starbucks.StarbucksNotFoundException;
import org.example.stellog.starbucks.domain.Starbucks;
import org.example.stellog.starbucks.domain.retpository.StarbucksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final MemberRoomValidator memberRoomValidator;
    private final ReviewRepository reviewRepository;
    private final StarbucksRepository starbucksRepository;
    private final StarbucksReviewRepository starbucksReviewRepository;
    private final ReviewMemberRepository reviewMemberRepository;

    @Transactional
    public void createReview(String email, Long roomId, ReviewRequestDto reviewRequestDto) {
        Member member = memberRoomValidator.findMemberByEmail(email);
        Room room = memberRoomValidator.findRoomById(roomId);
        memberRoomValidator.validateMemberInRoom(member, room);
        Starbucks starbucks = findStarbucksById(reviewRequestDto.starbucksId());

        Review review = Review.builder()
                .title(reviewRequestDto.title())
                .content(reviewRequestDto.content())
                .room(room)
                .build();

        ReviewMember reviewMember = ReviewMember.builder()
                .member(member)
                .review(review)
                .build();

        StarbucksReview starbucksReview = StarbucksReview.builder()
                .review(review)
                .starbucks(starbucks)
                .build();

        reviewRepository.save(review);
        reviewMemberRepository.save(reviewMember);
        starbucksReviewRepository.save(starbucksReview);
    }

    public ReviewListResponseDto getAllReviewsByStarbucksId(Long starbucksId) {
        Starbucks starbucks = findStarbucksById(starbucksId);

        List<StarbucksReview> starbucksReviews = starbucksReviewRepository.findAllByStarbucks(starbucks);

        List<ReviewResponseDto> reviewResponseDtos = starbucksReviews.stream()
                .map(StarbucksReview::getReview)
                .map(review -> ReviewResponseDto.builder()
                        .reviewId(review.getId())
                        .starbucksId(starbucksId)
                        .title(review.getTitle())
                        .content(review.getContent())
                        .build())
                .toList();

        return new ReviewListResponseDto(reviewResponseDtos);
    }

    public ReviewListResponseDto getAllReviewsByRoomId(String email, Long roomId) {
        Member member = memberRoomValidator.findMemberByEmail(email);
        Room room = memberRoomValidator.findRoomById(roomId);
        List<Review> reviews = reviewRepository.findAllByRoom(room);
        memberRoomValidator.validateMemberInRoom(member, room);

        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
                .map(review -> {
                    StarbucksReview starbucksReview = starbucksReviewRepository.findByReview(review)
                            .orElseThrow(StarbucksReviewNotFoundException::new);

                    Long starbucksId = starbucksReview.getStarbucks().getId();

                    return ReviewResponseDto.builder()
                            .reviewId(review.getId())
                            .starbucksId(starbucksId)
                            .title(review.getTitle())
                            .content(review.getContent())
                            .build();
                })
                .toList();

        return new ReviewListResponseDto(reviewResponseDtos);
    }

    public ReviewResponseDto getReviewById(Long reviewId) {
        Review review = findReviewById(reviewId);
        StarbucksReview starbucksReview = findStarbucksReviewByReview(review);

        return ReviewResponseDto.builder()
                .reviewId(review.getId())
                .starbucksId(starbucksReview.getStarbucks().getId())
                .title(review.getTitle())
                .content(review.getContent())
                .build();
    }

    @Transactional
    public void updateReview(String email, Long reviewId, ReviewRequestDto reviewRequestDto) {
        Member member = memberRoomValidator.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        ReviewMember reviewMember = reviewMemberRepository.findByReview(review)
                .orElseThrow(UnauthorizedReviewAccessException::new);

        validateReviewMember(member, review);

        review.updateReview(reviewRequestDto.title(), reviewRequestDto.content());
    }

    @Transactional
    public void deleteReview(String email, Long reviewId) {
        Member member = memberRoomValidator.findMemberByEmail(email);
        Review review = findReviewById(reviewId);
        StarbucksReview starbucksReview = findStarbucksReviewByReview(review);
        ReviewMember reviewMember = findReviewMemberByReview(review);

        validateReviewMember(member, review);

        reviewRepository.delete(review);
        reviewMemberRepository.delete(reviewMember);
        starbucksReviewRepository.delete(starbucksReview);
    }

    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);
    }

    private ReviewMember findReviewMemberByReview(Review review) {
        return reviewMemberRepository.findByReview(review)
                .orElseThrow(UnauthorizedReviewAccessException::new);
    }

    private void validateReviewMember(Member member, Review review) {
        ReviewMember reviewMember = findReviewMemberByReview(review);

        if (!reviewMember.getMember().equals(member)) {
            throw new UnauthorizedReviewAccessException("본인이 작성한 리뷰만 수정/삭제할 수 있습니다.");
        }
    }

    private StarbucksReview findStarbucksReviewByReview(Review review) {
        return starbucksReviewRepository.findByReview(review)
                .orElseThrow(StarbucksReviewNotFoundException::new);
    }

    private Starbucks findStarbucksById(Long starbucksId) {
        return starbucksRepository.findById(starbucksId)
                .orElseThrow(StarbucksNotFoundException::new);
    }
}
