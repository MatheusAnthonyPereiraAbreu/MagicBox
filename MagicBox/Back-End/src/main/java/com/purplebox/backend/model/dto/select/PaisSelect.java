package com.purplebox.backend.model.dto.select;

public enum PaisSelect implements Select {
    ID("id", Integer.class),
    NOME("nome", String.class),
    CODIGO("codigo", String.class);

    private final String attribute;
    private final Class<?> type;

    PaisSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}
