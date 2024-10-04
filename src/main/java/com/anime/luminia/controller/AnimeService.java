package com.anime.luminia.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnimeService {

    private final AnimeRepository animeRepository;

    public AnimeService(AnimeRepository animeRepository) {
        this.animeRepository = animeRepository;
    }

    // 검색 없는 기본 리스트 반환 (가나다순으로 정렬)
    public Page<Anime> getAnimeList(PageRequest pageRequest) {
        // 'koreanName'을 기준으로 오름차순 정렬
        PageRequest sortedPageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "koreanName"));
        return animeRepository.findAll(sortedPageRequest);
    }

    // 검색을 포함한 리스트 반환 (가나다순으로 정렬)
    public Page<Anime> searchAnimeList(String searchQuery, PageRequest pageRequest) {
        // 'koreanName'을 기준으로 오름차순 정렬
        PageRequest sortedPageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "koreanName"));
        return animeRepository.findByAlternateTitlesContainingIgnoreCase(searchQuery, sortedPageRequest);
    }

    // Anime 상세 정보 조회
    public Optional<Anime> getAnimeById(Long malId) {
        return animeRepository.findById(malId);
    }
}


