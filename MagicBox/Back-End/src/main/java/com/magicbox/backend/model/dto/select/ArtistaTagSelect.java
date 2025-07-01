package com.magicbox.backend.model.dto.select;

public enum ArtistaTagSelect implements Select {
    ARTISTA_ID("artistaId.id", Integer.class),
    TAG_ID("tagId.id", Integer.class);

    private final String attribute;
    private final Class<?> type;

    ArtistaTagSelect(String attribute, Class<?> type) {
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
