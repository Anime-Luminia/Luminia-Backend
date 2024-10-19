package com.anime.luminia.domain.review.dto;

import com.anime.luminia.domain.review.ReviewTier;

public record ReviewUpdateInput(
        String reviewId,
        String reviewText,
        ReviewTier tier,
        Boolean isSpoiler
) {}