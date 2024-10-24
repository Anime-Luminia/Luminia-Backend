package com.anime.luminia.domain.anime.service;

import com.anime.luminia.domain.anime.dto.AnimeListItemResponse;
import com.anime.luminia.domain.anime.dto.AnimeListResponse;
import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.repository.AnimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class AnimeService {

    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    @Cacheable(value = "animeListCache", key = "{#sortBy, #lastKoreanName, #lastMalId, #size}",
            condition = "#sortBy.equals('koreanName') && #lastKoreanName == null && #lastMalId == null")
    public AnimeListResponse getAnimeList(String sortBy, String lastKoreanName, Long lastMalId, Pageable pageable) {
        Slice<Anime> animeSlice = animeRepository.findAllByCursor(sortBy, lastKoreanName, lastMalId, pageable);

        List<AnimeListItemResponse> animeList = animeSlice.getContent().stream()
                .map(anime -> new AnimeListItemResponse(
                        anime.getMalId(),
                        anime.getKoreanName(),
                        anime.getProductionCompany(),
                        anime.getImageUrl(),
                        anime.getScore()
                ))
                .toList();

        return new AnimeListResponse(animeList, animeSlice.hasNext());
    }

    public AnimeListResponse searchAnimeList(String sortBy, String lastKoreanName, Long lastMalId, String searchQuery, Pageable pageable) {
        Slice<Anime> animeSlice = animeRepository.searchByTitles(sortBy, lastKoreanName, searchQuery, lastMalId, pageable);

        List<AnimeListItemResponse> animeList = animeSlice.getContent().stream()
                .map(anime -> new AnimeListItemResponse(
                        anime.getMalId(),
                        anime.getKoreanName(),
                        anime.getProductionCompany(),
                        anime.getImageUrl(),
                        anime.getScore()
                ))
                .toList();

        return new AnimeListResponse(animeList, animeSlice.hasNext());
    }

    // Anime 상세 정보 조회
    public Optional<Anime> getAnimeById(Long malId) {
        return animeRepository.findById(malId);
    }
}


