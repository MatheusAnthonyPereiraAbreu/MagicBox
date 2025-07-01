package com.purplebox.backend.model.dto.select;

public enum ArtistaSelect implements Select {
    ID("id", Integer.class),
    NOME("nome", String.class),
    OUVINTES_GLOBAIS("ouvintesGlobais", Long.class),
    PLAYCOUNT_GLOBAIS("playcountGlobais", Long.class);

    private final String attribute;
    private final Class<?> type;

    ArtistaSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}