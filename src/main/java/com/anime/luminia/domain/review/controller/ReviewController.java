package com.anime.luminia.domain.review.controller;

import com.anime.luminia.domain.auth.jwt.PrincipalDetails;
import com.anime.luminia.domain.review.Review;
import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.dto.ReviewPostInput;
import com.anime.luminia.domain.review.dto.ReviewUpdateInput;
import com.anime.luminia.domain.review.service.ReviewService;
import com.anime.luminia.domain.user.LuminiaUser;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @QueryMapping(name="getReviewsByAnime")
    public ReviewConnection getReviewsByAnime(
            @Argument String animeId,
            @Argument String after,
            @Argument int size,
            @Argument SortedOrder sortedOrder,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            DataFetchingEnvironment env) {

        boolean isFirstPage = after == null;
        boolean statsRequested = env.getSelectionSet().contains("stats");

        return reviewService.getReviewsByAnime(animeId, after, size, sortedOrder,
                isFirstPage && statsRequested, principalDetails);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createReview")
    public Review createReview(@Argument ReviewPostInput input, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return reviewService.createReview(input, principalDetails);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "updateReview")
    public Review updateReview(@Argument ReviewUpdateInput input, @AuthenticationPrincipal LuminiaUser user) {
        return reviewService.updateReview(input, user);
    }
//
//    @PreAuthorize("isAuthenticated()")
//    @MutationMapping(name = "deleteReview")
//    public Boolean deleteReview(@Argument String reviewId) {
//        return reviewService.deleteReview(reviewId);
//    }
}
