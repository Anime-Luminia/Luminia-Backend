package com.anime.luminia.domain.anime.dto;

import com.anime.luminia.domain.anime.entity.Anime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AnimeListResponse {
    private List<Anime> animes;
    private boolean hasNext;
}