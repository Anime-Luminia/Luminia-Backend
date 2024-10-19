package com.anime.luminia.domain.review.service;

import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.controller.ReviewConnection;
import com.anime.luminia.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewConnection getReviewsByAnime(String animeId, String after, int size, SortedOrder sortedOrder, boolean check){
        return reviewRepository.searchReviewCursorPagination(animeId, after, size, sortedOrder, check);
    }
}
