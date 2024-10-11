package com.anime.luminia.domain.anime.controller;

import com.anime.luminia.domain.anime.dto.AnimeListResponse;
import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.service.AnimeService;
import com.anime.luminia.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping("/list")
    public ApiResult<AnimeListResponse> getAnimeList(
            @RequestParam String sortBy,
            @RequestParam Integer size,
            @RequestParam(required = false) String lastKoreanName,
            @RequestParam(required = false) Long lastMalId,
            @RequestParam(required = false) String searchQuery) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<Anime> animePage;

        System.out.println(sortBy + " " + size + " " + lastKoreanName + " " + lastMalId + " " + searchQuery);

        if (searchQuery == null || searchQuery.isEmpty()) {
            animePage = animeService.getAnimeList(sortBy, lastKoreanName, lastMalId, pageable);
        } else {
            animePage = animeService.searchAnimeList(sortBy, lastKoreanName, lastMalId, searchQuery,pageable);
        }

        AnimeListResponse response = new AnimeListResponse(animePage.getContent(), animePage.hasNext());

        return ApiResult.success("애니 목록을 성공적으로 가져왔습니다.", response);
    }

    @GetMapping("/{malId}")
    public ResponseEntity<Anime> getAnimeById(@PathVariable Long malId) {
        return animeService.getAnimeById(malId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

