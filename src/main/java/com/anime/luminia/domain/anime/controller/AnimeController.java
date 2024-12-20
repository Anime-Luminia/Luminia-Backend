package com.anime.luminia.domain.anime.controller;

import com.anime.luminia.domain.anime.dto.AnimeListResponse;
import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.entity.type.AnimeGenreType;
import com.anime.luminia.domain.anime.entity.type.AnimeRating;
import com.anime.luminia.domain.anime.entity.type.AnimeType;
import com.anime.luminia.domain.anime.service.AnimeService;
import com.anime.luminia.global.dto.ApiResult;
import com.anime.luminia.global.error.exception.ErrorCode;
import com.anime.luminia.global.util.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/anime")
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeService animeService;

    @Timer
    @GetMapping("/list")
    public ResponseEntity<ApiResult<AnimeListResponse>> getAnimeList(
            @RequestParam String sortBy,
            @RequestParam Integer size,
            @RequestParam(required = false) String lastKoreanName,
            @RequestParam(required = false) Long lastMalId,
            @RequestParam(required = false) String searchQuery,
            @RequestParam(required = false) List<AnimeGenreType> includeGenres,
            @RequestParam(required = false) List<AnimeGenreType> excludeGenres,
            @RequestParam(required = false) List<AnimeType> types,
            @RequestParam(required = false) List<AnimeRating> ratings,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) String producer,
            @RequestParam(required = false) Boolean explicit) {

        Pageable pageable = PageRequest.of(0, size);
        AnimeListResponse animePage;

        if (searchQuery == null && includeGenres == null && excludeGenres == null
                && types == null && ratings == null && source == null
                && director == null && producer == null && explicit == null) {
            animePage = animeService.getAnimeList(sortBy, lastKoreanName, lastMalId, pageable);
        } else {
            animePage = animeService.searchAnimeList(sortBy, lastKoreanName, lastMalId, searchQuery, pageable);
        }


        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success("애니 목록을 성공적으로 가져왔습니다.", animePage));
    }

    @Timer
    @GetMapping("/{malId}")
    public ResponseEntity<ApiResult<Optional<Anime>>> getAnimeById(@PathVariable Long malId) {
        Optional<Anime> anime = animeService.getAnimeById(malId);

        if (anime.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResult.success("애니 정보를 성공적으로 가져왔습니다.", anime));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResult.failure(ErrorCode.ENTITY_NOT_FOUND));
        }
    }
}

