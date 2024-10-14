package com.anime.luminia.domain.anime.dto;

public record AnimeListItemResponse(
        Long malId,
        String koreanName,
        String productionCompany,
        String imageUrl,
        Double score
) {}