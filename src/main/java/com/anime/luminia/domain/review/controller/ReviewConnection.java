package com.anime.luminia.domain.review.controller;

import com.anime.luminia.domain.review.Review;
import com.anime.luminia.domain.review.dto.ReviewStats;
import com.anime.luminia.global.dto.CursorPageInfo;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ReviewConnection {
    private List<Review> reviews;
    private CursorPageInfo pageInfo;
    private ReviewStats stats;
}
