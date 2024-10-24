package com.anime.luminia.domain.anime.entity;


import com.anime.luminia.domain.review.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String alternateTitles;

    @Column(columnDefinition = "TEXT")
    private String special;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnimeGenre> genreMappings = new ArrayList<>();

//    @OneToOne(mappedBy = "anime", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private AnimePlot plot;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}


