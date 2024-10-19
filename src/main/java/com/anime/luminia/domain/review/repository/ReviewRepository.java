package com.anime.luminia.domain.review.repository;

import com.anime.luminia.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {

}


