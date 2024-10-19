package com.anime.luminia.domain.review.dto;

import lombok.Builder;

@Builder
public record ReviewStats(int totalReviews, float averageScore) {
}
