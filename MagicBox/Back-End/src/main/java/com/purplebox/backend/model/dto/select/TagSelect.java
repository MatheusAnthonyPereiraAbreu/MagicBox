package com.magicbox.backend.model.dto.select;

public enum TagSelect implements Select {
    ID("id", Integer.class),
    NOME("nome", String.class);

    private final String attribute;
    private final Class<?> type;

    TagSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}
