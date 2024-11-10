package com.anime.luminia.domain.review.service;

import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.repository.AnimeRepository;
import com.anime.luminia.domain.auth.jwt.PrincipalDetails;
import com.anime.luminia.domain.review.Review;
import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.controller.ReviewConnection;
import com.anime.luminia.domain.review.dto.ReviewPostInput;
import com.anime.luminia.domain.review.dto.ReviewUpdateInput;
import com.anime.luminia.domain.review.repository.ReviewRepository;
import com.anime.luminia.domain.user.LuminiaUser;
import com.anime.luminia.domain.user.LuminiaUserRepositroy;
import com.anime.luminia.global.error.exception.BusinessException;
import com.anime.luminia.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LuminiaUserRepositroy userRepository;
    private final AnimeRepository animeRepository;

    public ReviewConnection getReviewsByAnime(String animeId, String after, int size, SortedOrder sortedOrder,
                                              boolean check, PrincipalDetails principal){
        Long userId = principal == null ? null : principal.getUserId();

        return reviewRepository.searchReviewCursorPagination(animeId, after, size, sortedOrder, check, userId);
    }

    @Transactional
    public Review createReview(ReviewPostInput request, PrincipalDetails principal) {
        boolean check =  reviewRepository.existsByAnime_MalIdAndLuminiaUser_Id(request.animeId(), principal.getUserId());

        if(check) throw new BusinessException(ErrorCode.REVIEW_EXIST);

        Anime anime = animeRepository.findByMalId(request.animeId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        if(request.reviewText().trim().isEmpty() || request.reviewText().length() > 1000){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        LuminiaUser user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        Review review = Review.builder()
                .reviewText(request.reviewText())
                .tier(request.tier().getScore())
                .isSpoiler(request.isSpoiler())
                .anime(anime)
                .luminiaUser(user)
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        return review;
    }

    @Transactional
    public Review updateReview(ReviewUpdateInput request, LuminiaUser user){
        Review review = reviewRepository.findByIdAndAnime_MalIdAndLuminiaUser_Id(request.reviewId(), request.animeId(), user.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.REVIEW_EXIST));

        if(request.reviewText().trim().equals("") || request.reviewText().length() > 1000 || request.reviewText().length() < 50){
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        review = review.updateReview(request.reviewText(), request.tier(), request.isSpoiler());

        return reviewRepository.save(review);
    }
}
