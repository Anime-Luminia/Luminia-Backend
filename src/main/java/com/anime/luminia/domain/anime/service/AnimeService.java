package com.anime.luminia.domain.anime.service;

import com.anime.luminia.domain.anime.dto.AnimeListResponse;
import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.repository.AnimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    @Cacheable(value = "animeListCache", key = "{#sortBy, #lastKoreanName, #lastMalId, #size}")
    public AnimeListResponse getAnimeList(String sortBy, String lastKoreanName, Long lastMalId, Pageable pageable) {
        Slice<Anime> anime = animeRepository.findAllByCursor(sortBy, lastKoreanName, lastMalId, pageable);

        return new AnimeListResponse(anime.getContent(), anime.hasNext());
    }

    public AnimeListResponse searchAnimeList(String sortBy, String lastKoreanName, Long lastMalId, String searchQuery, Pageable pageable) {
        Slice<Anime> anime = animeRepository.searchByTitles(sortBy, lastKoreanName, searchQuery, lastMalId, pageable);
        return new AnimeListResponse(anime.getContent(), anime.hasNext());
    }

    // Anime 상세 정보 조회
    public Optional<Anime> getAnimeById(Long malId) {
        return animeRepository.findById(malId);
    }
}


