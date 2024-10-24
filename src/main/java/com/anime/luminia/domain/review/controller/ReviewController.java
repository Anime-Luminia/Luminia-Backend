package com.anime.luminia.domain.review.controller;

import com.anime.luminia.domain.review.Review;
import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.dto.ReviewPostInput;
import com.anime.luminia.domain.review.dto.ReviewUpdateInput;
import com.anime.luminia.domain.review.service.ReviewService;
import com.anime.luminia.domain.user.LuminiaUser;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

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
            @AuthenticationPrincipal LuminiaUser user,
            DataFetchingEnvironment env) {

        boolean isFirstPage = after == null;
        boolean statsRequested = env.getSelectionSet().contains("stats");

        System.out.println(after + " " + isFirstPage + " " + statsRequested);

        ReviewConnection reviewConnection = reviewService.getReviewsByAnime(animeId, after, size, sortedOrder,
                isFirstPage && statsRequested, user);

        return reviewConnection;
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createReview")
    public Review createReview(@Argument ReviewPostInput input, @AuthenticationPrincipal LuminiaUser user) {
        return reviewService.createReview(input, user);
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
