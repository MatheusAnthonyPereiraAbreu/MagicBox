package com.magicbox.backend.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.magicbox.backend.model.dto.select.*;
import com.magicbox.backend.model.dto.table.Table;

import java.io.IOException;

public abstract class GenericDeserializer<T> extends StdDeserializer<T> {

    public Class<T> clazz;

    public GenericDeserializer(Class<T> clazz) {
        super(clazz);
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return null; // Implementado nas subclasses
    }

    protected Select convertField(Table table, String fieldStr) {
        return switch (table) {
            case ARTISTA -> ArtistaSelect.valueOf(fieldStr);
            case ALBUM -> AlbumSelect.valueOf(fieldStr);
            case MUSICA -> MusicaSelect.valueOf(fieldStr);
            case PAIS -> PaisSelect.valueOf(fieldStr);
            case TAG -> TagSelect.valueOf(fieldStr);
            case USUARIO -> UsuarioSelect.valueOf(fieldStr);
            case RANKING_ARTISTAS -> RankingAtualArtistasPaisesSelect.valueOf(fieldStr);
            case RANKING_MUSICAS -> RankingAtualMusicasPaisesSelect.valueOf(fieldStr);
        };
    }
}
