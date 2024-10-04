package com.anime.luminia.controller;


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
    private String alternateTitles;
}


