package com.magicbox.backend.model.dto.select;

public enum UsuarioSelect implements Select {
    ID("id", Integer.class),
    NOME("nome", String.class);

    private final String attribute;
    private final Class<?> type;

    UsuarioSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}
