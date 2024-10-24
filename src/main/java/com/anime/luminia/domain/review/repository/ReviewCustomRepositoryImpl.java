package com.anime.luminia.domain.review.repository;


import com.anime.luminia.domain.review.QReview;
import com.anime.luminia.domain.review.Review;
import com.anime.luminia.domain.review.SortedOrder;
import com.anime.luminia.domain.review.controller.ReviewConnection;
import com.anime.luminia.domain.review.dto.ReviewStats;
import com.anime.luminia.domain.user.LuminiaUser;
import com.anime.luminia.global.dto.CursorPageInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ReviewConnection searchReviewCursorPagination(String animeId, String after, int size, SortedOrder sortedOrder,
                                                         boolean check, LuminiaUser user) {
        QReview review = QReview.review;
        BooleanExpression predicate = review.anime.malId.eq(Long.parseLong(animeId));

        OrderSpecifier<?> sortOrder;
        if(sortedOrder == SortedOrder.LATEST){
            sortOrder = review.createdAt.desc();
        }
        else{
            sortOrder = review.createdAt.asc();
        }

        if(after != null && !after.isEmpty()){
            predicate = predicate.and(review.id.gt(Long.parseLong(after)));
        }

        if(user != null){
            predicate = predicate.and(review.luminiaUser.ne(user));
        }

        List<Review> reviews = jpaQueryFactory.selectFrom(review)
                .where(predicate)
                .orderBy(sortOrder)
                .limit(size)
                .fetch();

        CursorPageInfo pageInfo = new CursorPageInfo(reviews.size() == size);

        ReviewStats stats = null;
        Optional<Review> myReview = Optional.empty();

        if (check) {
            stats = calculateReviewStats(animeId);

            if(user != null){
                Review reviewResult = jpaQueryFactory.selectFrom(review)
                        .where(review.luminiaUser.eq(user).and(review.anime.malId.eq(Long.valueOf(animeId))))
                        .fetchOne();

                myReview = Optional.ofNullable(reviewResult);
            }
        }

        return new ReviewConnection(reviews, pageInfo, stats, myReview.orElse(null));
    }

    private ReviewStats calculateReviewStats(String animeId) {
        return new ReviewStats(100, 4.5f);
    }
}
