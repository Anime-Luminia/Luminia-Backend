package com.anime.luminia.domain.review;

public enum SortedOrder {
    LATEST("Latest"),
    RECOMMENDED("Recommended");

    private final String displayName;

    SortedOrder(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
