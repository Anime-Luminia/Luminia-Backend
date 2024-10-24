package com.anime.luminia.domain.review.dto;

import com.anime.luminia.domain.review.ReviewTier;

public record ReviewPostInput(
        Long animeId,
        String reviewText,
        ReviewTier tier,
        Boolean isSpoiler
) {}