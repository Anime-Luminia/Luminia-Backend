package com.anime.luminia.domain.review.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {

    private boolean hasNextPage;
    private String endCursor;

    public PageInfo(boolean hasNextPage, String endCursor) {
        this.hasNextPage = hasNextPage;
        this.endCursor = endCursor;
    }

    // Getters and setters
}