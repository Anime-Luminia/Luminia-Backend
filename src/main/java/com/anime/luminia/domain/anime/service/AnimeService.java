package com.anime.luminia.domain.anime.service;

import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.repository.AnimeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnimeService {

    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    public Slice<Anime> getAnimeList(String sortBy, String lastKoreanName, Long lastMalId, Pageable pageable) {
        return animeRepository.findAllByCursor(sortBy, lastKoreanName, lastMalId, pageable);
    }

    public Slice<Anime> searchAnimeList(String sortBy, String lastKoreanName, Long lastMalId, String searchQuery, Pageable pageable) {
        return animeRepository.searchByTitles(sortBy, lastKoreanName, searchQuery, lastMalId, pageable);
    }

    // Anime 상세 정보 조회
    public Optional<Anime> getAnimeById(Long malId) {
        return animeRepository.findById(malId);
    }
}


