package com.purplebox.backend.model.dto.select;

public enum AlbumSelect implements Select {
    ID("id", Integer.class),
    NOME("nome", String.class),
    PLAYCOUNT_GLOBAIS("playcountGlobais", Long.class),
    ARTISTA_ID("artista.id", Integer.class);

    private final String attribute;
    private final Class<?> type;

    AlbumSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}