package com.anime.luminia.domain.review.repository;

import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.controller.ReviewConnection;

public interface ReviewCustomRepository {
    ReviewConnection searchReviewCursorPagination(String animeId, String after, int size, SortedOrder sortedOrder, boolean check);

}
