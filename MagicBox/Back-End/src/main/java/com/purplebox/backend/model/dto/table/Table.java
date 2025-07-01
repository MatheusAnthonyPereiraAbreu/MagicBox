package com.purplebox.backend.model.dto.table;

import com.purplebox.backend.model.entity.*;
import com.purplebox.backend.model.dto.Selectable;

public enum Table {

    ARTISTA("artista"),
    ALBUM("album"),
    MUSICA("musica"),
    PAIS("pais"),
    TAG("tag"),
    USUARIO("usuario"),
    RANKING_ARTISTAS("ranking_atual_artistas_paises"),
    RANKING_MUSICAS("ranking_atual_musicas_paises");

    private final String attribute;

    Table(String attribute) {
        this.attribute = attribute;
    }

    public String attribute() {
        return attribute;
    }

    public Class<? extends Selectable> toSelectable() {
        return switch (this) {
            case ARTISTA -> Artista.class;
            case ALBUM -> Album.class;
            case MUSICA -> Musica.class;
            case PAIS -> Pais.class;
            case TAG -> Tag.class;
            case USUARIO -> Usuario.class;
            case RANKING_ARTISTAS -> RankingAtualArtistasPaises.class;
            case RANKING_MUSICAS -> RankingAtualMusicasPaises.class;
        };
    }
}
