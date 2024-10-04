package com.anime.luminia.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/anime")
public class AnimeController {

    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    @GetMapping("/list")
    public Page<Anime> getAnimeList(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(required = false) String searchQuery) {
        PageRequest pageRequest = PageRequest.of(page, size);
        if (searchQuery != null && !searchQuery.isEmpty()) {
            return animeService.searchAnimeList(searchQuery, pageRequest);
        } else {
            return animeService.getAnimeList(pageRequest);
        }
    }

    @GetMapping("/{malId}")
    public ResponseEntity<Anime> getAnimeById(@PathVariable Long malId) {
        return animeService.getAnimeById(malId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

