package com.anime.luminia.domain.review.repository;

import com.anime.luminia.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {
    boolean existsByAnime_MalIdAndLuminiaUser_Id(Long animeId, Long userId);
    Optional<Review> findByIdAndAnime_MalIdAndLuminiaUser_Id(Long id, Long animeId, Long userId);
}


