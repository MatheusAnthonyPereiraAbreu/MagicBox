package com.magicbox.backend.model.dto.table;

import com.magicbox.backend.model.entity.*;
import com.magicbox.backend.model.dto.Selectable;

public enum Table {

    ARTISTA("artista"),
    ALBUM("album"),
    MUSICA("musica"),
    PAIS("pais"),
    TAG("tag"),
    USUARIO("usuario"),
    RANKING_ARTISTAS("ranking_atual_artistas_paises"),
    ARTISTA_TAG("artista_tag"),
    SIMILARIDADE_ARTISTA("similaridade_artista"),
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
            case ARTISTA_TAG -> ArtistaTag.class;
            case SIMILARIDADE_ARTISTA -> SimilaridadeArtista.class;
            case RANKING_ARTISTAS -> RankingAtualArtistasPaises.class;
            case RANKING_MUSICAS -> RankingAtualMusicasPaises.class;
        };
    }
}
