package com.purplebox.backend.model.dto.select;

public enum RankingAtualMusicasPaisesSelect implements Select {
    MUSICA_ID("musica.id", Integer.class),
    PAIS_ID("pais.id", Integer.class),
    POSICAO_RANKING("posicaoRanking", Short.class),
    DATA_ULTIMA_ATUALIZACAO("dataUltimaAtualizacao", java.time.LocalDateTime.class);

    private final String attribute;
    private final Class<?> type;

    RankingAtualMusicasPaisesSelect(String attribute, Class<?> type) {
        this.attribute = attribute;
        this.type = type;
    }

    public String attribute() { return attribute; }
    public Class<?> type() { return type; }
}
