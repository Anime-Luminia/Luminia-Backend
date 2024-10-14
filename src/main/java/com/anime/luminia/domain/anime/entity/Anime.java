package com.anime.luminia.domain.anime.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "anime")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimeGenre> genreMappings = new ArrayList<>();

    @OneToOne(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnimePlot plot;
}


