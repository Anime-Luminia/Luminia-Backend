package com.anime.luminia.domain.anime.entity.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnimeType {
    TV("TV"),
    ONA("ONA"),
    OVA("OVA"),
    MOVIE("Movie");

    private final String typeName;

    AnimeType(String typeName) {
        this.typeName = typeName;
    }

    @JsonValue
    public String getTypeName() {
        return typeName;
    }
}
