package com.anime.luminia.domain.anime.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimeListResponse {
    private List<AnimeListItemResponse> animes;
    private boolean hasNext;
}