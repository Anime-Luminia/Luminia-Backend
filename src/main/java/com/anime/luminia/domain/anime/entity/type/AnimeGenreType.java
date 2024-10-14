package com.anime.luminia.domain.anime.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum AnimeGenreType {
    ACTION("액션", "Action"),
    ADVENTURE("모험", "Adventure"),
    COMEDY("코미디", "Comedy"),
    DRAMA("드라마", "Drama"),
    ECCHI("에치", "Ecchi"),
    FANTASY("판타지", "Fantasy"),
    HORROR("공포", "Horror"),
    MAGICAL_GIRL("마법 소녀", "Magical Girl"),
    MECHA("메카", "Mecha"),
    MUSIC("음악", "Music"),
    MYSTERY("미스터리", "Mystery"),
    PSYCHOLOGICAL("심리", "Psychological"),
    ROMANCE("로맨스", "Romance"),
    SCIFI("SF", "Sci-Fi"),
    SLICE_OF_LIFE("일상", "Slice of Life"),
    SPORTS("스포츠", "Sports"),
    SUPERNATURAL("초자연", "Supernatural"),
    THRILLER("스릴러", "Thriller"),
    ;

    private final String koreanName;
    @Getter
    private final String englishName;

    AnimeGenreType(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    @JsonValue
    public String getKoreanName() {
        return koreanName;
    }

    @JsonCreator
    public static AnimeGenreType fromKoreanName(String koreanName) {
        for (AnimeGenreType genre : AnimeGenreType.values()) {
            if (genre.getKoreanName().equalsIgnoreCase(koreanName)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown genre: " + koreanName);
    }
}
