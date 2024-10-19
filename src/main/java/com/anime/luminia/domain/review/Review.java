package com.anime.luminia.domain.review;

import com.anime.luminia.domain.anime.entity.Anime;
import com.anime.luminia.domain.user.LuminiaUser;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewTier tier;

    @Column(nullable = false)
    private Boolean isSpoiler = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 관계 설정: 리뷰와 애니메이션 간의 관계 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    // 관계 설정: 리뷰와 작성자 간의 관계 (다대일)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private LuminiaUser luminiaUser;
}

