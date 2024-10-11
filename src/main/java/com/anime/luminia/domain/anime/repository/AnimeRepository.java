package com.anime.luminia.domain.anime.repository;

import com.anime.luminia.domain.anime.entity.Anime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnimeRepository extends JpaRepository<Anime, Long>, AnimeCustomRepository {

    Page<Anime> findAll(Pageable pageable);

    Page<Anime> findByAlternateTitlesContainingIgnoreCase(String title, Pageable pageable);

    Optional<Anime> findByMalId(Long malId);
}


