package com.anime.luminia.domain.review.controller;

import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.service.ReviewService;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
            DataFetchingEnvironment env) {

        boolean isFirstPage = after == null;
        boolean statsRequested = env.getSelectionSet().contains("stats");

        ReviewConnection reviewConnection = reviewService.getReviewsByAnime(animeId, after, size, sortedOrder, isFirstPage && statsRequested);

        return reviewConnection;
    }

//    @PreAuthorize("isAuthenticated()")
//    @MutationMapping(name = "createReview")
//    public Review createReview(@Argument ReviewPostInput input) {
//        return reviewService.createReview(input);
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @MutationMapping(name = "updateReview")
//    public Review updateReview(@Argument ReviewUpdateInput input) {
//        return reviewService.updateReview(input);
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @MutationMapping(name = "deleteReview")
//    public Boolean deleteReview(@Argument String reviewId) {
//        return reviewService.deleteReview(reviewId);
//    }
}
