package com.magicbox.backend.model.dto.select;

public enum RankingAtualArtistasPaisesSelect implements Select {
    ARTISTA_ID("artista.id", Integer.class),
    PAIS_ID("pais.id", Integer.class),
    POSICAO_RANKING("posicaoRanking", Short.class),
    DATA_ULTIMA_ATUALIZACAO("dataUltimaAtualizacao", java.time.LocalDateTime.class);

    private final String attribute;
    private final Class<?> type;

    RankingAtualArtistasPaisesSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}
