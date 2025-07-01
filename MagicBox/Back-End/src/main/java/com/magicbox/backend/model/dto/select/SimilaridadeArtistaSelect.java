package com.magicbox.backend.model.dto.select;

public enum SimilaridadeArtistaSelect implements Select {
    ARTISTA_BASE_ID("artistaBaseId.id", Integer.class),
    ARTISTA_SIMILAR_ID("artistaSimilarId.id", Integer.class);

    private final String attribute;
    private final Class<?> type;

    SimilaridadeArtistaSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() {
        return attribute;
    }

    public Class<?> type() {
        return type;
    }
}