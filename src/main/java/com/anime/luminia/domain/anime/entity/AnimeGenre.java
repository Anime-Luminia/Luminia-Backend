package com.anime.luminia.domain.anime.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "anime_genre")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimeGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;
}
