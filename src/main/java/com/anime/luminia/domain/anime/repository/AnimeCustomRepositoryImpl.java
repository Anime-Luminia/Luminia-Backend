package com.anime.luminia.domain.anime.repository;

import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.anime.entity.QAnime;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AnimeCustomRepositoryImpl implements AnimeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Slice<Anime> findAllByCursor(String sortBy, String lastKoreanName, Long lastMalId, Pageable pageable) {
        QAnime anime = QAnime.anime;

        List<Anime> results;

        switch (sortBy) {
            case "popularity":
                results = jpaQueryFactory
                        .selectFrom(anime)
                        .where(lastKoreanName == null ? null : anime.koreanName.gt(lastKoreanName)
                                .or(anime.koreanName.eq(lastKoreanName).and(anime.malId.gt(lastMalId))))
                        .orderBy(anime.score.desc(), anime.koreanName.asc(), anime.malId.asc())
                        .limit(pageable.getPageSize() + 1)
                        .fetch();
                break;
            default:
                results = jpaQueryFactory
                        .selectFrom(anime)
                        .where(lastKoreanName == null ? null : anime.koreanName.gt(lastKoreanName))
                        .orderBy(anime.koreanName.asc(), anime.malId.asc())
                        .limit(pageable.getPageSize() + 1)
                        .fetch();
                break;
        }

        return createSlice(results, pageable);
    }

    @Override
    public Slice<Anime> searchByTitles(String sortBy, String lastKoreanName, String searchQuery, Long cursorId, Pageable pageable) {
        QAnime anime = QAnime.anime;

        searchQuery = searchQuery.toLowerCase().replaceAll("\\s+", "");

        BooleanExpression baseCondition = Expressions.booleanTemplate(
                "lower(replace(cast({0} as string), ' ', '')) like lower({1})", anime.alternateTitles, "%" + searchQuery + "%"
        );

        if ("popularity".equals(sortBy)) baseCondition = baseCondition.and(cursorId == null ? null : anime.malId.gt(cursorId));
        else baseCondition = baseCondition.and(lastKoreanName == null ? null : anime.koreanName.gt(lastKoreanName));

        JPAQuery<Anime> query = jpaQueryFactory
                .selectFrom(anime)
                .where(baseCondition)
                .limit(pageable.getPageSize() + 1);

        if ("popularity".equals(sortBy)) query.orderBy(anime.score.desc(), anime.koreanName.asc(), anime.malId.asc());
        else query.orderBy(anime.koreanName.asc(), anime.malId.asc());

        List<Anime> results = query.fetch();

        return createSlice(results, pageable);
    }

    private Slice<Anime> createSlice(List<Anime> results, Pageable pageable) {
        boolean hasNext = results.size() > pageable.getPageSize();
        if (hasNext) {
            results.remove(results.size() - 1);
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }
}
