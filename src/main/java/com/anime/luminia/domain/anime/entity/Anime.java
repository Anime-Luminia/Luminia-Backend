package com.anime.luminia.domain.anime.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "anime")
@Getter
public class Anime {

    @Id
    private Long malId;
    private String koreanName;
    private String japanesesName;
    private String productionCompany;
    private String genre;
    private String themes;
    private String animelistUrl;
    private String imageUrl;
    private Double score;
    private String source;
    private String largeImageUrl;
    private String trailerUrl;
    private String rating;

    @Column(columnDefinition = "TEXT")
    private String alternateTitles;

    @Column(columnDefinition = "TEXT")
    private String special;
}


