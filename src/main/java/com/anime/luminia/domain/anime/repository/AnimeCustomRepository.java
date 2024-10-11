package com.anime.luminia.domain.anime.repository;

import com.anime.luminia.domain.anime.entity.Anime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AnimeCustomRepository {
    Slice<Anime> findAllByCursor(String sortBy, String lastKoreanName, Long lastMalId, Pageable pageable);

    Slice<Anime> searchByTitles(String sortBy, String lastKoreanName, String searchQuery, Long cursorId, Pageable pageable);
}
