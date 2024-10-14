package com.anime.luminia.domain.anime.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AnimeRating {
    GENERAL("전체 이용가", "General"),
    PG_13("13세 이상", "PG-13"),
    R_17("17세 이상", "R-17"),
    R_19("19세 이상", "R-19");

    private final String ratingName;
    @Getter
    private final String englishName;

    AnimeRating(String ratingName, String englishName) {
        this.ratingName = ratingName;
        this.englishName = englishName;
    }

    @JsonValue
    public String getRatingName() {
        return ratingName;
    }

    @JsonCreator
    public static AnimeRating fromRatingName(String ratingName) {
        for (AnimeRating rating : AnimeRating.values()) {
            if (rating.getRatingName().equalsIgnoreCase(ratingName)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Unknown rating: " + ratingName);
    }
}
