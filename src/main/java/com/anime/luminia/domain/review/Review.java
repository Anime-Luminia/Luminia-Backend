package com.anime.luminia.domain.review;

import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.user.LuminiaUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "anime_review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String reviewText;

    @Column(nullable = false)
    private double tier;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isSpoiler = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private LuminiaUser luminiaUser;

    public Review updateReview(String reviewText, ReviewTier tier, Boolean isSpoiler) {
        this.reviewText = reviewText;
        this.tier = tier.getScore();
        this.isSpoiler = isSpoiler;
        return this;
    }
}

