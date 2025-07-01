package com.magicbox.backend.model.dto.select;

public enum MusicaSelect implements Select {
    ID("id", Integer.class),
    NOME("nome", String.class),
    DURACAO_FAIXA("duracaoFaixa", Integer.class),
    ARTISTA_ID("artista.id", Integer.class),
    ALBUM_ID("album.id", Integer.class);

    private final String attribute;
    private final Class<?> type;

    MusicaSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}
