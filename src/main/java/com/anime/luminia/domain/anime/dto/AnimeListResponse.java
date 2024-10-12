package com.anime.luminia.domain.anime.dto;

import com.anime.luminia.domain.anime.entity.Anime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnimeListResponse {
    private List<Anime> animes;
    private boolean hasNext;
}